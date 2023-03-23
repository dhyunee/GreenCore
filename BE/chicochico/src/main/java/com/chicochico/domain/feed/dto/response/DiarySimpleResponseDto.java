package com.chicochico.domain.feed.dto.response;


import com.chicochico.domain.feed.entity.DiaryEntity;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


/**
 * diaryId: Long
 * content: String,
 * tags: List<String>,
 * observationDate: LocalDate
 * createdAt: LocalDateTime
 * imagePath: String
 * commentCount: Integer
 */
@Data
@Builder
public class DiarySimpleResponseDto {

	private Long diaryId;
	private String content;
	private List<String> tags;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate observationDate;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime craetedAt;
	private String imagePath;
	private Integer commentCount;


	public static DiarySimpleResponseDto fromEntity(DiaryEntity diary, Function<Long, List<String>> getTagsList) {
		return DiarySimpleResponseDto.builder()
			.diaryId(diary.getId())
			.content(diary.getContent())
			.tags(getTagsList.apply(diary.getId()))
			.observationDate(diary.getObservationDate())
			.craetedAt(diary.getCreatedAt())
			.imagePath(diary.getImagePath())
			.commentCount(diary.getCommentCount())
			.build();
	}


	public static List<DiarySimpleResponseDto> fromEnityList(List<DiaryEntity> diaryList, Function<Long, List<String>> getTagsList) {
		List<DiarySimpleResponseDto> result = new ArrayList<>();
		for (DiaryEntity diary : diaryList) {
			DiarySimpleResponseDto diarySimpleResponseDto = DiarySimpleResponseDto.fromEntity(diary, getTagsList);
			result.add(diarySimpleResponseDto);
		}
		return result;
	}


	public static Page<DiarySimpleResponseDto> fromEnityPage(Page<DiaryEntity> page, Pageable pageable, Function<Long, List<String>> getTagsList) {
		List<DiaryEntity> entityList = new ArrayList<>(page.toList());
		List<DiarySimpleResponseDto> dtoList = DiarySimpleResponseDto.fromEnityList(entityList, getTagsList);
		Page<DiarySimpleResponseDto> result = new PageImpl<>(dtoList, pageable, dtoList.size());
		return result;
	}

}
