# sam_retagger.py

import json
import os
import argparse
from transformers import pipeline
from tqdm import tqdm

def load_entries(file_path):
    with open(file_path, "r", encoding="utf-8") as f:
        return [json.loads(line) for line in f]

def save_entries(entries, out_path):
    os.makedirs(os.path.dirname(out_path), exist_ok=True)
    with open(out_path, "w", encoding="utf-8") as f:
        for entry in entries:
            f.write(json.dumps(entry, ensure_ascii=False) + "\n")

def tag_mood(entries, debug=False):
    classifier = pipeline("text-classification", model="bhadresh-savani/distilbert-base-uncased-emotion", device=-1)
    for entry in tqdm(entries, desc="Tagging mood"):
        text = entry.get("text") or entry.get("content") or ""
        if not text.strip():
            entry["mood"] = "unknown"
            continue
        try:
            result = classifier(text[:512])[0]
            entry["mood"] = result["label"].lower()
            if debug:
                print(f"\n[Mood] Text: {text[:100]}...\n→ {entry['mood']}")
        except Exception as e:
            entry["mood"] = "error"
    return entries

def tag_emotion(entries, debug=False):
    classifier = pipeline("text-classification", model="j-hartmann/emotion-english-distilroberta-base", device=-1)
    for entry in tqdm(entries, desc="Tagging emotion"):
        text = entry.get("text") or entry.get("content") or ""
        if not text.strip():
            entry["emotion"] = "unknown"
            continue
        try:
            result = classifier(text[:512])[0]
            entry["emotion"] = result["label"].lower()
            if debug:
                print(f"\n[Emotion] Text: {text[:100]}...\n→ {entry['emotion']}")
        except Exception as e:
            entry["emotion"] = "error"
    return entries

def tag_energy_tier(entries):
    for entry in tqdm(entries, desc="Tagging energyTier"):
        mood = entry.get("mood", "unknown")
        # Dumb tiering logic – customize as needed
        low_energy = {"sadness", "fear", "anger"}
        high_energy = {"joy", "love", "surprise"}
        entry["energyTier"] = "high" if mood in high_energy else "low" if mood in low_energy else "neutral"
    return entries

def tag_neutral_state(entries):
    for entry in tqdm(entries, desc="Tagging neutral/stateTag"):
        mood = entry.get("mood", "")
        if mood in {"neutral", "calm"}:
            entry["neutral"] = True
            entry["stateTag"] = "neutral"
        else:
            entry["neutral"] = False
            entry["stateTag"] = "charged"
    return entries

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--file", type=str, required=True, help="Input .jsonl file")
    parser.add_argument("--debug", action="store_true")
    parser.add_argument("--tag", nargs="+", choices=["mood", "emotion", "energy", "neutral"], default=["mood"], help="Tagging options")

    args = parser.parse_args()
    entries = load_entries(args.file)

    if "mood" in args.tag:
        entries = tag_mood(entries, args.debug)
    if "emotion" in args.tag:
        entries = tag_emotion(entries, args.debug)
    if "energy" in args.tag:
        entries = tag_energy_tier(entries)
    if "neutral" in args.tag:
        entries = tag_neutral_state(entries)

    out_file = args.file.replace("sam_logs_output", "sam_logs_cleaned")
    save_entries(entries, out_file)

    print(f"\n✅ Saved cleaned file to: {out_file}")

if __name__ == "__main__":
    main()