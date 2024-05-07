def compare_files_ordered(file1, file2):
    with open(file1, 'r') as f1, open(file2, 'r') as f2:
        file1_lines = f1.readlines()
        file2_lines = f2.readlines()

    # Find the length of the shortest file
    min_length = min(len(file1_lines), len(file2_lines))

    # Initialize containers for differences
    differences = []

    # Check line by line for differences up to the length of the shortest file
    for i in range(min_length):
        if file1_lines[i] != file2_lines[i]:
            differences.append((i + 1, file1_lines[i], file2_lines[i]))  # Store line number and differing lines

    # Check if one file is longer than the other
    if len(file1_lines) > min_length:
        for i in range(min_length, len(file1_lines)):
            differences.append((i + 1, file1_lines[i], None))  # Extra lines in file1

    elif len(file2_lines) > min_length:
        for i in range(min_length, len(file2_lines)):
            differences.append((i + 1, None, file2_lines[i]))  # Extra lines in file2

    return differences

# Replace 'output.txt' and 'small5.txt' with the paths to your actual files
file1 = 'output.txt'
file2 = 'real.txt'

differences = compare_files_ordered(file1, file2)

if differences:
    print("Differences found:")
    for line_number, line1, line2 in differences:
        if line1 is not None and line2 is not None:
            print(f"Line {line_number}:")
            print(f"{file1}: {line1.strip()}")
            print(f"{file2}: {line2.strip()}")
        elif line1:
            print(f"Line {line_number} in {file1} is not present in {file2}:")
            print(line1.strip())
        elif line2:
            print(f"Line {line_number} in {file2} is not present in {file1}:")
            print(line2.strip())
else:
    print("The files are identical (including line order).")
