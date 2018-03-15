package com.tw.leewin.esclientdemo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class News {
  private String title;
  private String tag;
  private String publishTime;
}
