for file in target/*.jar; do
  mv "$file" "${file%.*}-ubuntu.${file##*.}"
done
