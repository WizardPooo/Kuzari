package com.kuzari.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Teleport {
    private final double x;
    private final double y;
    private final double z;

    private final long creationTime = System.currentTimeMillis();
}
