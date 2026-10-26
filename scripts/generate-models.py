#!/usr/bin/env python3
"""Generate typed Jackson beans from the pinned OpenAPI schema definitions."""
import copy
import html
import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
schemas = json.loads((ROOT / 'contract/openapi.json').read_text())['components']['schemas']
contract_count = len(schemas)
schemas = copy.deepcopy(schemas)
# The official CLI browser exchange is a separate, documented website protocol.
schemas['AuthorizationResult'] = {'type': 'object', 'properties': {
    name: {'type': 'string'} for name in
    ['actor', 'identity_name', 'display_name', 'auth_provider', 'token_id', 'access_token']
}, 'description': 'Approved browser-code authorization, including the secret access token. Applications own secure storage.'}
schemas['OAuthError']['properties']['error_description'] = {'type': 'string', 'description': 'Untrusted OAuth recovery guidance.'}
KEYWORDS = set('abstract assert boolean break byte case catch char class const continue default do double else enum extends final finally float for goto if implements import instanceof int interface long native new package private protected public record return short static super switch synchronized this throw throws transient try void volatile while'.split())


def ident(value):
    value = re.sub(r'[_-]([a-z])', lambda match: match[1].upper(), value)
    return value + 'Value' if value in KEYWORDS else value


def title(value):
    value = ident(value)
    return value[0].upper() + value[1:]


def words(value):
    return re.sub(r'([a-z])([A-Z])', r'\1 \2', value).replace('_', ' ').lower()


def doc(description, tags=(), indent='    '):
    text = html.escape(' '.join(description.split())).replace('*/', '* /')
    return [indent + '/**', indent + ' * ' + text, *[indent + ' * ' + tag for tag in tags], indent + ' */']


def dereference(schema):
    return schemas[schema['$ref'].split('/')[-1]] if '$ref' in schema else schema


def merge_variants(variants):
    """Represent object unions as typed variant fields without wire wrappers."""
    variants = [dereference(value) for value in variants if value.get('type') != 'null']
    if len(variants) == 1:
        return copy.deepcopy(variants[0])
    if all('properties' in value or value.get('type') == 'object' for value in variants):
        properties = {}
        for variant in variants:
            for key, value in variant.get('properties', {}).items():
                if key in properties and properties[key] != value:
                    properties[key] = {'oneOf': [properties[key], value]}
                else:
                    properties[key] = copy.deepcopy(value)
        return {'type': 'object', 'properties': properties}
    # Compatible scalar variants differ only in constraints or enum values.
    kinds = {scalar_kind(value) for value in variants}
    if len(kinds) == 1:
        return {'type': kinds.pop()}
    return {}


def scalar_kind(schema):
    kind = schema.get('type')
    if isinstance(kind, list):
        kind = next((value for value in kind if value != 'null'), 'null')
    if kind is None and ('enum' in schema or 'const' in schema):
        values = schema.get('enum', [schema.get('const')])
        if all(isinstance(value, str) for value in values):
            kind = 'string'
        elif all(isinstance(value, bool) for value in values):
            kind = 'boolean'
        elif all(isinstance(value, int) for value in values):
            kind = 'integer'
    return kind


def normalized(schema):
    variants = schema.get('oneOf', schema.get('anyOf'))
    if variants:
        nonnull = [value for value in variants if value.get('type') != 'null']
        if len(nonnull) == 1:
            return normalized(nonnull[0])
        resolved = [normalized(dereference(value)) for value in nonnull]
        if resolved and all(value == resolved[0] for value in resolved):
            return resolved[0]
        return normalized(merge_variants(resolved))
    return schema


def typ(schema, hint):
    if '$ref' in schema:
        return schema['$ref'].split('/')[-1]
    schema = normalized(schema)
    if '$ref' in schema:
        return schema['$ref'].split('/')[-1]
    kind = scalar_kind(schema)
    if kind == 'array':
        return 'List<' + typ(schema.get('items', {}), hint + 'Item') + '>'
    if 'properties' in schema:
        if hint not in schemas:
            schemas[hint] = schema
        return hint
    if kind == 'object':
        values = schema.get('additionalProperties')
        return 'Map<String, ' + (typ(values, hint + 'Value') if isinstance(values, dict) else 'Object') + '>'
    return {'string': 'String', 'integer': 'Long', 'number': 'BigDecimal', 'boolean': 'Boolean', 'null': 'Void'}.get(kind, 'Object')


# Discover all named and inline object shapes before rendering Java source.
objects = {}
scalars = {}
queue = list(schemas)
for name in queue:
    schema = copy.deepcopy(normalized(schemas[name]))
    if schemas[name].get('description'):
        schema['description'] = schemas[name]['description']
    if 'oneOf' in schemas[name] or 'anyOf' in schemas[name]:
        schema['description'] = schema.get('description', 'Typed union object.') + ' Known variant fields share one wire object; the server validates variant-specific requirements.'
    if 'properties' in schema or scalar_kind(schema) == 'object':
        properties = schema.get('properties', {})
        objects[name] = (schema, [(wire, typ(prop, name + title(wire)), prop) for wire, prop in properties.items()])
    else:
        scalars[name] = (schema, typ(schema, name + 'Value'))
    for discovered in schemas:
        if discovered not in queue:
            queue.append(discovered)

lines = ['package app.bibleit;', '',
    'import com.fasterxml.jackson.annotation.JsonAnySetter;',
    'import com.fasterxml.jackson.annotation.JsonCreator;',
    'import com.fasterxml.jackson.annotation.JsonProperty;',
    'import com.fasterxml.jackson.annotation.JsonValue;',
    'import com.fasterxml.jackson.core.type.TypeReference;',
    'import java.math.BigDecimal;',
    'import java.util.Collections;',
    'import java.util.HashSet;',
    'import java.util.LinkedHashMap;',
    'import java.util.List;',
    'import java.util.Map;',
    'import java.util.Objects;',
    'import java.util.Set;', '',
    '/** Typed Jackson beans; unions expose their known variant fields without wire wrappers. */',
    'public final class Models {', '    private Models() {}']
lines += doc('Common wire-field presence and unknown-field support for typed Java beans.')
lines += ['    public abstract static class Model {',
    '        private final Set<String> present = new HashSet<>();',
    '        private final Map<String, Object> additional = new LinkedHashMap<>();']
lines += doc('Creates an object with no wire fields set.', indent='        ')
lines += ['        protected Model() {}']
base = [
    ('Tests wire presence, distinguishing an omitted property from explicit null.', ['@param name the wire property name', '@return true when the property was supplied'], 'public boolean hasField(String name) { return present.contains(name); }'),
    ('Removes a property and resets a known Java field to null.', ['@param name the wire property name'], 'public void removeField(String name) { clearKnownField(name); present.remove(name); additional.remove(name); }'),
    ('Assigns a known typed property or retains an unknown extension property.', ['@param name the wire property name', '@param value a Java value; null is an explicit JSON null'], '@JsonAnySetter public void setField(String name, Object value) { Objects.requireNonNull(name); if (!assignKnownField(name, value)) { additional.put(name, value); present.add(name); } }'),
    ('Returns the unknown server properties as ordinary Java values.', ['@return an unmodifiable snapshot of additional properties'], 'public Map<String, Object> additionalProperties() { return Collections.unmodifiableMap(new LinkedHashMap<>(additional)); }'),
    ('Returns a wire-property snapshot containing typed Java values.', ['@return the property snapshot; nested objects retain normal Java reference semantics'], '@JsonValue public Map<String, Object> toMap() { var values = new LinkedHashMap<>(additional); appendFields(values); return Collections.unmodifiableMap(values); }'),
    ('Records that a typed setter supplied a property.', ['@param name the wire property name'], 'protected void markField(String name) { present.add(name); }'),
    ('Collects known properties that were explicitly set.', ['@param fields the output property snapshot'], 'protected abstract void appendFields(Map<String, Object> fields);'),
    ('Assigns an extension-style value to a known typed property.', ['@param name the wire property name', '@param value the Java value', '@return true when the name identifies a known property'], 'protected abstract boolean assignKnownField(String name, Object value);'),
    ('Resets a known Java property without marking it present.', ['@param name the wire property name'], 'protected abstract void clearKnownField(String name);'),
]
for description, tags, code in base:
    lines += doc(description, tags, '        ') + ['        ' + code]
lines += [
    '        @Override public boolean equals(Object other) { return other != null && getClass() == other.getClass() && toMap().equals(((Model) other).toMap()); }',
    '        @Override public int hashCode() { return Objects.hash(getClass(), toMap()); }',
    '        @Override public String toString() { return getClass().getSimpleName() + "[redacted]"; }', '    }']
for name, (schema, properties) in objects.items():
    lines += doc(schema.get('description') or 'Java representation of ' + words(name) + '.')
    lines += ['    public static final class ' + name + ' extends Model {']
    for wire, java_type, prop in properties:
        lines += ['        private ' + java_type + ' ' + ident(wire) + ';']
    lines += doc('Creates an object with all properties unset.', indent='        ')
    lines += ['        public ' + name + '() {}']
    for wire, java_type, prop in properties:
        field = ident(wire)
        suffix = title(wire)
        lines += doc(prop.get('description') or 'Returns ' + words(wire) + '.', ['@return the typed Java value, or null when absent or explicitly null'], '        ')
        lines += ['        @JsonProperty(' + json.dumps(wire) + ') public ' + java_type + ' get' + suffix + '() { return ' + field + '; }']
        lines += doc('Sets ' + words(wire) + '.', ['@param value the Java value; null writes explicit JSON null'], '        ')
        lines += ['        @JsonProperty(' + json.dumps(wire) + ') public void set' + suffix + '(' + java_type + ' value) { ' + field + ' = value; markField(' + json.dumps(wire) + '); }']
    lines += ['        @Override protected void appendFields(Map<String, Object> fields) {']
    lines += ['            if (hasField(' + json.dumps(wire) + ')) fields.put(' + json.dumps(wire) + ', ' + ident(wire) + ');' for wire, _, _ in properties]
    lines += ['        }', '        @Override protected boolean assignKnownField(String name, Object value) {', '            return switch (name) {']
    lines += ['                case ' + json.dumps(wire) + ' -> { set' + title(wire) + '(Json.MAPPER.convertValue(value, new TypeReference<' + java_type + '>() {})); yield true; }' for wire, java_type, _ in properties]
    lines += ['                default -> false;', '            };', '        }', '        @Override protected void clearKnownField(String name) {', '            switch (name) {']
    lines += ['                case ' + json.dumps(wire) + ' -> ' + ident(wire) + ' = null;' for wire, _, _ in properties]
    lines += ['                default -> { }', '            }', '        }', '    }']
for name, (schema, java_type) in scalars.items():
    lines += doc(schema.get('description') or 'Typed scalar value for ' + words(name) + '.')
    lines += ['    public static final class ' + name + ' {', '        private final ' + java_type + ' value;']
    lines += doc('Creates a typed scalar without introducing a wire wrapper.', ['@param value the scalar Java value'], '        ')
    lines += ['        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public ' + name + '(' + java_type + ' value) { this.value = Objects.requireNonNull(value); }']
    lines += doc('Returns the scalar wire value.', ['@return the Java scalar value'], '        ')
    lines += ['        @JsonValue public ' + java_type + ' value() { return value; }',
        '        @Override public boolean equals(Object other) { return other instanceof ' + name + ' scalar && value.equals(scalar.value); }',
        '        @Override public int hashCode() { return value.hashCode(); }',
        '        @Override public String toString() { return "' + name + '[redacted]"; }', '    }']
lines += ['}']
(ROOT / 'src/main/java/app/bibleit/Models.java').write_text('\n'.join(lines) + '\n')
print(f'Generated {contract_count} contract models plus typed browser authorization and inline object shapes')
