import os
import json

def search_logs(folder, keywords):
    keywords = [kw.lower() for kw in keywords]
    matches = []

    for filename in os.listdir(folder):
        if not filename.endswith(".jsonl"):
            continue
        with open(os.path.join(folder, filename), 'r', encoding='utf-8') as f:
            for line in f:
                try:
                    entry = json.loads(line)
                    text = entry.get("text", "").lower()
                    if all(k in text for k in keywords):
                        matches.append(entry)
                except:
                    continue

    return matches

if __name__ == "__main__":
    folder = "sam_logs_cleaned"
    keywords = ["emi", "break", "weak"]
    results = search_logs(folder, keywords)

    print(f"✅ Found {len(results)} matching entries.\n")
    for i, entry in enumerate(results[:10]):
        print(f"{i+1}. [{entry.get('date', 'no-date')}] {entry.get('text')[:120]}")
        print("-" * 80)