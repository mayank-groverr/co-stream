package practice.mayank.co_stream.dto;

import java.util.List;

public record KeysetPage<T>(List<T> items, boolean hasMore) {
}