import json
import sys
data = {"spam" : "foo", "parrot" : 42}
in_json = json.dumps(data)
def test():
    return in_json

print(sys.path)