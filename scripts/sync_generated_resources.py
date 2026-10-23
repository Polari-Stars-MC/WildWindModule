"""Copy datagen output into its owning release module; split shared tags/maps/languages.

Run after :datagen:runClientData and :datagen:runServerData. Hand-authored
resources not emitted by datagen are preserved. No local paths or dependencies.
"""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
MODULES = ("core", "wilderness", "barren")


def owner(value):
    for module in MODULES:
        if "wild_wind_" + module in value:
            return module
    # Vanilla potion translation keys and sound subtitles have no mod namespace.
    if any(token in value for token in ("reach", "piranha", "mudcrab")):
        return "wilderness"
    return "core"


def save(module, relative, value, merge=False):
    path = ROOT / "src" / module / "resources" / relative
    path.parent.mkdir(parents=True, exist_ok=True)
    if merge and path.exists():
        value = json.loads(path.read_text(encoding="utf-8")) | value
    path.write_text(json.dumps(value, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")


def main():
    count = 0
    for side in ("client", "server"):
        directory = ROOT / "build/generated/datagen" / side
        if not directory.is_dir():
            raise SystemExit(f"Missing {directory}; run both datagen tasks first.")
        for path in sorted(directory.rglob("*.json")):
            relative = path.relative_to(directory)
            if relative.parts[0] not in ("assets", "data"):
                continue
            data = json.loads(path.read_text(encoding="utf-8"))
            if "lang" in relative.parts:
                groups = {module: {} for module in MODULES}
                for key, value in data.items():
                    if "fletching_table" not in key and "arrow_component" not in key:
                        groups[owner(key)][key] = value
                for module, entries in groups.items():
                    save(module, f"assets/wild_wind_{module}/lang/{path.name}", entries, merge=True)
            elif relative.parts[1] in ("minecraft", "neoforge", "c") and ("tags" in relative.parts or "data_maps" in relative.parts):
                maps = "data_maps" in relative.parts
                groups = {module: {} if maps else [] for module in MODULES}
                if maps:
                    for key, value in data["values"].items():
                        groups[owner(key)][key] = value
                else:
                    for value in data["values"]:
                        groups[owner(value if isinstance(value, str) else value["id"])].append(value)
                for module, values in groups.items():
                    if values:
                        save(module, relative, data | {"values": values})
                    else:
                        (ROOT / "src" / module / "resources" / relative).unlink(missing_ok=True)
            else:
                save(owner(str(relative)), relative, data)
            count += 1
    print(f"Synced {count} generated resources into three release modules.")


if __name__ == "__main__":
    main()
