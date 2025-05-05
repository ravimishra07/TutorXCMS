#!/bin/bash

# Path to your logs
LOG_DIR="sam_logs_output"

# Loop from April (03) to January (01)
for MONTH in  04 03 02 01
do
    FILE="$LOG_DIR/2025-${MONTH}.jsonl"
    echo "Processing $FILE"
    python sam_retagger.py --file "$FILE" --debug
done