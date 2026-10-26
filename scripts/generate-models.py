#!/usr/bin/env python3
"""Generate Java wrappers and models from contract/openapi.json."""
import html
import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
spec = json.loads((ROOT / 'contract/openapi.json').read_text())
out = ROOT / 'src/main/java/app/bibleit'
KEYWORDS = set('abstract assert boolean break byte case catch char class const continue default do double else enum extends final finally float for goto if implements import instanceof int interface long native new package private protected public record return short static super switch synchronized this throw throws transient try void volatile while'.split())


def ident(value):
    value = re.sub(r'[_-]([a-z])', lambda match: match[1].upper(), value)
    return value + 'Value' if value in KEYWORDS else value


def words(value):
    return re.sub(r'([a-z])([A-Z])', r'\1 \2', value).replace('_', ' ').lower()


def typ(schema):
    if '$ref' in schema:
        return schema['$ref'].split('/')[-1]
    kind = schema.get('type')
    if isinstance(kind, list):
        kind = next((item for item in kind if item != 'null'), None)
    if kind == 'array':
        return 'List<' + typ(schema.get('items', {})) + '>'
    return {'string': 'String', 'integer': 'Long', 'number': 'BigDecimal', 'boolean': 'Boolean'}.get(kind, 'JsonNode')


def doc(description, tags=(), indent='    '):
    text = html.escape(' '.join(description.split())).replace('*/', '* /')
    return [indent + '/**', indent + ' * ' + text, *[indent + ' * ' + tag for tag in tags], indent + ' */']


model = ['package app.bibleit;', '', 'import com.fasterxml.jackson.annotation.JsonCreator;', 'import com.fasterxml.jackson.annotation.JsonValue;', 'import com.fasterxml.jackson.core.type.TypeReference;', 'import com.fasterxml.jackson.databind.JsonNode;', 'import com.fasterxml.jackson.databind.node.ObjectNode;', 'import java.math.BigDecimal;', 'import java.util.List;', '', '/** Java schema beans preserving missing fields, explicit nulls and unknown fields. */', 'public final class Models {', '    private Models() {}']
model += doc('Base object model retaining the complete wire representation.')
model += ['    public abstract static class Model {', '        private final ObjectNode fields;']
model += doc('Creates an empty JSON object.', indent='        ')
model += ['        protected Model() { fields = Json.MAPPER.createObjectNode(); }']
model += doc('Copies a JSON object into this model.', ['@param value the object to copy', '@throws IllegalArgumentException if the value is not an object'], '        ')
model += ['        protected Model(JsonNode value) {', '            if (!value.isObject()) throw new IllegalArgumentException("Expected a JSON object");', '            fields = (ObjectNode) value.deepCopy();', '        }']
base_methods = [
    ('Returns a defensive copy of all wire fields.', ['@return the copied JSON object'], '@JsonValue public JsonNode toJson() { return fields.deepCopy(); }'),
    ('Checks whether a field is present, including explicit null.', ['@param name the wire field name', '@return true when the field exists'], 'public boolean hasField(String name) { return fields.has(name); }'),
    ('Removes a field so it is omitted when serialized.', ['@param name the wire field name'], 'public void removeField(String name) { fields.remove(name); }'),
    ('Sets an arbitrary wire field while preserving future server fields.', ['@param name the wire field name', '@param value the copied value; null stores an explicit JSON null'], 'public void setField(String name, JsonNode value) { fields.set(name, value == null ? Json.MAPPER.nullNode() : value.deepCopy()); }'),
    ('Decodes one field into its Java representation.', ['@param <T> the field type', '@param name the wire field name', '@param type the Jackson target type', '@return the decoded field value'], 'protected <T> T get(String name, TypeReference<T> type) { return Json.MAPPER.convertValue(fields.get(name), type); }'),
    ('Serializes a Java field into the wire representation.', ['@param name the wire field name', '@param value the field value; null stores an explicit JSON null'], 'protected void set(String name, Object value) { fields.set(name, Json.MAPPER.valueToTree(value)); }'),
]
for description, tags, code in base_methods:
    model += doc(description, tags, '        ') + ['        ' + code]
model += ['        @Override public String toString() { return getClass().getSimpleName() + "[redacted]"; }', '    }']
for name, schema in spec['components']['schemas'].items():
    model += doc(schema.get('description') or 'Wire representation of the ' + words(name) + ' schema.')
    properties = schema.get('properties')
    if properties is not None or schema.get('type') == 'object':
        model += ['    public static final class ' + name + ' extends Model {']
        model += doc('Creates an empty object; unset fields are omitted when serialized.', indent='        ')
        model += ['        public ' + name + '() {}']
        model += doc('Creates a model from the complete JSON object, retaining unknown fields.', ['@param value the wire object to copy'], '        ')
        model += ['        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public ' + name + '(JsonNode value) { super(value); }']
        for wire, prop in (properties or {}).items():
            java_name = ident(wire)
            suffix = java_name[0].upper() + java_name[1:]
            description = prop.get('description') or 'The ' + words(wire) + ' value.'
            model += doc('Returns ' + words(wire) + '. ' + description, ['@return the decoded field value; use hasField to test wire presence'], '        ')
            model += ['        public ' + typ(prop) + ' get' + suffix + '() { return get(' + json.dumps(wire) + ', new TypeReference<>() {}); }']
            model += doc('Sets ' + words(wire) + '.', ['@param value the Java value; null writes an explicit JSON null'], '        ')
            model += ['        public void set' + suffix + '(' + typ(prop) + ' value) { set(' + json.dumps(wire) + ', value); }']
        model += ['    }']
    else:
        model += ['    public static final class ' + name + ' {', '        private final JsonNode value;']
        model += doc('Copies a scalar or union wire value without adding wrapper fields.', ['@param value the non-null JSON value'], '        ')
        model += ['        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public ' + name + '(JsonNode value) { this.value = value.deepCopy(); }']
        model += doc('Returns the underlying wire representation.', ['@return a defensive copy of the JSON value'], '        ')
        model += ['        @JsonValue public JsonNode value() { return value.deepCopy(); }', '        @Override public String toString() { return "' + name + '[redacted]"; }', '    }']
model += ['}']
(out / 'Models.java').write_text('\n'.join(model) + '\n')
print(f'Generated {len(spec["components"]["schemas"])} schema models')
