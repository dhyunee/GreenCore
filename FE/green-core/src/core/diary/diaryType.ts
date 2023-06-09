import { NextRouter } from 'next/router';

// 일지 타입
export type DiaryType = {
  diaryId: number;
  content: string;
  tags: Array<string>;
  opservationDate: string;
  createdAt: string;
  imagePath: FormData;
  commentCount: number;
};

// 일지 생성 타입
export type CreateDiaryType = {
  router: NextRouter;
  diarySetId: number;
  payload: FormData;
};

// 일지 삭제
export type DeleteDiaryType = {
  router: NextRouter;
  payload: {
    diaryId: number;
    diarySetId: number;
  };
};

// 일지 수정
export type UpdateDiaryType = {
  router: NextRouter;
  payload: FormData;
  diaryId: Number;
};
