package com.github.kacperpotapczyk.pvoptimizer.backend.dto.task;

public record TaskBaseObjectRevisionDto(String baseName, long revisionNumber) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskBaseObjectRevisionDto that)) return false;

        if (revisionNumber != that.revisionNumber) return false;
        return baseName.equals(that.baseName);
    }

    @Override
    public int hashCode() {
        int result = baseName.hashCode();
        result = 31 * result + (int) (revisionNumber ^ (revisionNumber >>> 32));
        return result;
    }
}
