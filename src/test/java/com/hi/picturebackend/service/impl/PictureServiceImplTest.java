package com.hi.picturebackend.service.impl;

import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

class PictureServiceImplTest {

    @Test
    void clearPictureFile() {
        String url = "https://myqcloud.com/public/2010648136322/2026-01-23_EjY2VFjh0zhxo.webp";

        URI uri = URI.create(url);
        String key = uri.getPath();
        System.out.println(key);
    }
}