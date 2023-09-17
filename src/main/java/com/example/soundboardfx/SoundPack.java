package com.example.soundboardfx;

import java.io.Serializable;

public record SoundPack(String author, String title, String creationDate, String description, String soundFileName) implements Serializable {
}
