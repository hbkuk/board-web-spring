package com.study.ebsoft.model.file;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FileOriginalName {
    private static final String IMAGE_NAME_EXTENSION__REGEX = "\\.(\\w+)$";
    private static final Pattern EXTENSION_PATTERN_COMPILE = Pattern.compile(IMAGE_NAME_EXTENSION__REGEX);
    private String originalName;

    public FileOriginalName(String fileName) {

        if (isInvalidImageName(fileName)) {
            throw new IllegalArgumentException("유효하지 않은 확장자입니다.");
        }

        this.originalName = fileName;
    }

    private boolean isInvalidImageName(String fileName) {
        Matcher matcher = getMatcher(fileName);
        if (!matcher.find()) {
            return true;
        }
        if (!FileNameExtension.contains(matcher.group(1).toUpperCase())) {
            return true;
        }
        return false;
    }

    public String getOriginalName() {
        return originalName;
    }

    private Matcher getMatcher(String imageName) {
        return EXTENSION_PATTERN_COMPILE.matcher(imageName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        FileOriginalName imageName1 = (FileOriginalName) o;
        return Objects.equals(originalName, imageName1.originalName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(originalName);
    }
}
