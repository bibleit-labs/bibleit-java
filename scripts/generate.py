#!/usr/bin/env python3
"""Generate Java wrappers and models from contract/openapi.json; no server checkout required."""
import json
import re
from pathlib import Path
ROOT = Path(__file__).resolve().parent.parent
spec = json.loads((ROOT / 'contract/openapi.json').read_text())
out = ROOT / 'src/main/java/app/bibleit'
KEYWORDS = set('abstract assert boolean break byte case catch char class const continue default do double else enum extends final finally float for goto if implements import instanceof int interface long native new package private protected public record return short static super switch synchronized this throw throws transient try void volatile while'.split())
def ident(s):
    s = re.sub(r'[_-]([a-z])', lambda m: m[1].upper(), s)
    return s + 'Value' if s in KEYWORDS else s
def typ(s):
    if '$ref' in s: return s['$ref'].split('/')[-1]
    t = s.get('type')
    if isinstance(t, list): t = next((x for x in t if x != 'null'), None)
    if t == 'array': return 'List<' + typ(s.get('items', {})) + '>'
    return {'string': 'String', 'integer': 'Long', 'number': 'BigDecimal', 'boolean': 'Boolean'}.get(t, 'JsonNode')
lines = ['package app.bibleit;', '', 'import java.util.Map;', '', '/** Generated operation directory. */', 'final class Operations {', '    private Operations() {}', '    static final Map<String, Operation> ALL = Map.ofEntries(']
methods = []
count = 0
for path, item in spec['paths'].items():
    for verb, op in item.items():
        if not isinstance(op, dict) or 'operationId' not in op: continue
        count += 1
        name = op['operationId']
        params = re.findall(r'{([^}]+)}', path)
        lines.append('        Map.entry(' + json.dumps(name) + ', new Operation(' + json.dumps(verb.upper()) + ', ' + json.dumps(path) + ', ' + json.dumps(op.get('summary', '')) + ')),')
        args = ', '.join('String ' + ident(p) for p in params)
        mapping = 'Map.of(' + ', '.join(json.dumps(p) + ', ' + ident(p) for p in params) + ')'
        methods += ['    /** ' + op.get('summary', name).replace('*/', '') + '. */', '    public Response ' + name + '(' + args + (', ' if args else '') + 'Options options) throws IOException, InterruptedException {', '        return call("' + name + '", ' + mapping + ', options);', '    }', '    public Response ' + name + '(' + args + ') throws IOException, InterruptedException {', '        return ' + name + '(' + ', '.join(ident(p) for p in params) + (', ' if params else '') + 'Options.defaults());', '    }']
lines[-1] = lines[-1].rstrip(',')
lines += ['    );', '}']
(out / 'Operations.java').write_text('\n'.join(lines) + '\n')
(out / 'BibleitClient.java').write_text((ROOT / 'scripts/client.header').read_text() + '\n'.join(methods) + '\n}\n')
model = ['package app.bibleit;', '', 'import com.fasterxml.jackson.annotation.*;', 'import com.fasterxml.jackson.core.type.TypeReference;', 'import com.fasterxml.jackson.databind.JsonNode;', 'import com.fasterxml.jackson.databind.node.ObjectNode;', 'import java.math.BigDecimal;', 'import java.util.*;', '', '/** Generated Java beans. Missing fields, explicit nulls and unknown fields are retained. */', 'public final class Models {', '    private Models() {}', '    public abstract static class Model {', '        private final ObjectNode fields;', '        protected Model() { fields = Json.MAPPER.createObjectNode(); }', '        protected Model(JsonNode value) {', '            if (!value.isObject()) throw new IllegalArgumentException("Expected a JSON object");', '            fields = (ObjectNode) value.deepCopy();', '        }', '        @JsonValue public JsonNode toJson() { return fields.deepCopy(); }', '        public boolean hasField(String name) { return fields.has(name); }', '        public void removeField(String name) { fields.remove(name); }', '        public void setField(String name, JsonNode value) { fields.set(name, value == null ? Json.MAPPER.nullNode() : value.deepCopy()); }', '        protected <T> T get(String name, TypeReference<T> type) { return Json.MAPPER.convertValue(fields.get(name), type); }', '        protected void set(String name, Object value) { fields.set(name, Json.MAPPER.valueToTree(value)); }', '        @Override public String toString() { return getClass().getSimpleName() + "[redacted]"; }', '    }']
for name, schema in spec['components']['schemas'].items():
    props = schema.get('properties')
    if props is not None or schema.get('type') == 'object':
        model += ['    public static final class ' + name + ' extends Model {', '        public ' + name + '() {}', '        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public ' + name + '(JsonNode value) { super(value); }']
        for wire, prop in (props or {}).items():
            java_name = ident(wire)
            suffix = java_name[0].upper() + java_name[1:]
            model += ['        public ' + typ(prop) + ' get' + suffix + '() { return get(' + json.dumps(wire) + ', new TypeReference<>() {}); }', '        public void set' + suffix + '(' + typ(prop) + ' value) { set(' + json.dumps(wire) + ', value); }']
        model += ['    }']
    else:
        model += ['    public static final class ' + name + ' {', '        private final JsonNode value;', '        @JsonCreator(mode = JsonCreator.Mode.DELEGATING) public ' + name + '(JsonNode value) { this.value = value.deepCopy(); }', '        @JsonValue public JsonNode value() { return value.deepCopy(); }', '        @Override public String toString() { return "' + name + '[redacted]"; }', '    }']
model += ['}']
(out / 'Models.java').write_text('\n'.join(model) + '\n')
print(f'Generated {count} operation wrappers and {len(spec["components"]["schemas"])} models')
