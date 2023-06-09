package com.chicochico.domain.feed.controller;


import com.chicochico.common.dto.ResultDto;
import com.chicochico.common.service.AuthService;
import com.chicochico.domain.feed.dto.response.FeedResponseDto;
import com.chicochico.domain.feed.dto.response.FeedSimpleResponseDto;
import com.chicochico.domain.feed.entity.FeedEntity;
import com.chicochico.domain.feed.service.FeedService;
import com.chicochico.domain.user.entity.UserEntity;
import com.chicochico.domain.user.service.FollowService;
import com.chicochico.exception.CustomException;
import com.chicochico.exception.ErrorCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
@Api(tags = "피드 관련 API")
public class FeedController {

	private final FeedService feedService;

	private final AuthService authService;

	private final FollowService followService;


	@GetMapping
	@ApiOperation(value = "피드 추천 목록을 조회합니다.", notes = "")
	public ResponseEntity<ResultDto<Page<FeedResponseDto>>> getFeedList(@PageableDefault Pageable pageable) {
		List<FeedEntity> feedEntityList = feedService.getRecommendedFeedList(pageable);
		Page<FeedResponseDto> feedResponseDtoPage = FeedResponseDto.fromEnityPage(feedEntityList, feedService::isLikedFeed, feedService::getCommentCount, followService::isFollowed,
			feedService::getTagContentList, pageable);
		return ResponseEntity.ok().body(ResultDto.of(feedResponseDtoPage));
	}


	@GetMapping("/follow")
	@ApiOperation(value = "팔로우한 사람의 최신 피드 목록을 조회합니다.", notes = "")
	public ResponseEntity<ResultDto<Page<FeedResponseDto>>> getFeedListByFollowUser(Pageable pageable) {
		String nickname = authService.getUserNickname();
		List<UserEntity> followingList = followService.getFollowingList(nickname);

		Page<FeedEntity> feedEntityPage = feedService.getFeedListByFollowUser(followingList, pageable);
		Page<FeedResponseDto> feedResponseDtoPage = FeedResponseDto.fromEnityPage(feedEntityPage, feedService::isLikedFeed, feedService::getCommentCount, followService::isFollowed,
			feedService::getTagContentList);
		return ResponseEntity.ok().body(ResultDto.of(feedResponseDtoPage));
	}


	@GetMapping("/tag")
	@ApiOperation(value = "태그로 피드를 검색한 결과를 조회합니다.", notes = "")
	public ResponseEntity<ResultDto<Page<FeedSimpleResponseDto>>> getFeedList(@RequestParam("search") String tag, Pageable pageable) {
		Page<FeedEntity> feedEntityPage = feedService.getFeedListByTag(tag, pageable);
		Page<FeedSimpleResponseDto> feedResponseDtoPage = FeedSimpleResponseDto.fromEnityPage(feedEntityPage);

		int page = pageable.getPageNumber();
		if (page != 0 && feedResponseDtoPage.getTotalPages() <= page) {
			throw new CustomException(ErrorCode.PAGE_NOT_FOUND);
		}

		return ResponseEntity.ok().body(ResultDto.of(feedResponseDtoPage));
	}


	@PostMapping("/{feedId}/like")
	@ApiOperation(value = "피드 좋아요를 누릅니다.", notes = "")
	public ResponseEntity<ResultDto<Boolean>> createFeedLike(@PathVariable("feedId") Long feedId) {
		feedService.createFeedLike(feedId);

		return ResponseEntity.ok().body(ResultDto.ofSuccess());
	}


	@DeleteMapping("/{feedId}/like")
	@ApiOperation(value = "피드 좋아요를 취소합니다.", notes = "")
	public ResponseEntity<ResultDto<Boolean>> deleteFeedLike(@PathVariable("feedId") Long feedId) {
		feedService.deleteFeedLike(feedId);

		return ResponseEntity.ok().body(ResultDto.ofSuccess());
	}

}
