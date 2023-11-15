package com.poscodx.msa.service.guestbook.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GuestbookVo {
	private Long no;
	private String name;
	private String password;
	private String regDate;
	private String contents;
}