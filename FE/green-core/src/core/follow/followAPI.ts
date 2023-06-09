import { createAsyncThunk } from '@reduxjs/toolkit';
import http from '@/lib/http';
import { scrollType } from '@/core/follow/followType';
import { encodeNickname } from '@/lib/utils';

// 팔로우하기
export const updateFollow = async (nickname: string | string[]) => {
  try {
    console.log(nickname);
    const { data } = await http.post(`/following/${encodeNickname(nickname)}`);
    return data;
  } catch (error) {
    console.error(error);
  }
};

// 언팔로우하기
export const deleteFollow = async (nickname: string | string[]) => {
  try {
    const { data } = await http.delete(`/following/${encodeNickname(nickname)}`);
    return data;
  } catch (error) {
    console.error(error);
  }
};

export const deleteFollower = async (nickname: string | string[]) => {
  try {
    const { data } = await http.delete(`/follower/${encodeNickname(nickname)}`);
    return data;
  } catch (error) {
    console.error(error);
  }
};

// 팔로잉 리스트 가져오기
export const getFollowingList = async (nickname: string | string[], params: scrollType) => {
  try {
    const { data } = await http.get(`/following/${encodeNickname(nickname)}`, { params });
    console.log(data);
    return data;
  } catch (error) {
    console.error(error);
  }
};

// 팔로워 리스트 가져오기
export const getFollowerList = async (nickname: string | string[], params: scrollType) => {
  try {
    const { data } = await http.get(`/follower/${encodeNickname(nickname)}`, { params });
    return data;
  } catch (error) {
    console.error(error);
  }
};
