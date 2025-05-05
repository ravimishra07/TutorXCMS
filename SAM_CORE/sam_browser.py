import argparse
from utils.loader import load_logs

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--file", required=True, help="Path to .jsonl log file")
    parser.add_argument("--search", required=False, help="Search keyword (optional)")
    args = parser.parse_args()

    logs = load_logs(args.file)

    for entry in logs:
        if args.search:
            if args.search.lower() in entry.get("text", "").lower():
                print(f"[{entry.get('timestamp', 'N/A')}] {entry['text']}")
        else:
            print(f"[{entry.get('timestamp', 'N/A')}] {entry['text']}")

if __name__ == "__main__":
    main()