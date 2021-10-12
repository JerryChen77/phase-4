package com.qf.myspringbootredisdemo.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Site implements Serializable {

  private long id;
  private String name;
}
