package com.hi.picturebackend.model.dto.picture;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PictureTagCategory implements Serializable {

    public List<String> TagList;

    public List<String> CategoryList;

    private static final long serialVersionUID = 1L;
}
