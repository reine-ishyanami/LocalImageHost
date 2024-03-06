for file in target/*.jar; do
  mv "$file" "${file%.*}-macos.${file##*.}"
done
