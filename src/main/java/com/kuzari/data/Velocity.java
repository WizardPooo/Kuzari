package com.kuzari.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Velocity {

    private final double horizontal, vertical;

    private final long creationTime = System.currentTimeMillis();
}