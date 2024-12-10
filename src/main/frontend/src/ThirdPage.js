import React, { useState, useEffect } from "react";
import styled, { createGlobalStyle } from "styled-components";

import { useNavigate, useLocation } from "react-router-dom";

import axios from "axios";

function ThirdPage() {
  const { state } = useLocation();
  const [diaries, setDiaries] = useState([]);
  const navigate = useNavigate();
  const [isReversed, setIsReversed] = useState(false);

  const handleDiaryClick = (diaries) => {
    navigate("/ShowDiary", {
      state: {
        id: diaries.id,
        text: diaries.content,
        imageSrc: diaries.imageSrc,
      },
    });
  };

  const GoCalender = () => {
    navigate("/Calender");
  };

  const toggleOrder = () => {
    setIsReversed(!isReversed);
  };

  const handleDeleteDiary = async (id, event) => {
    event.stopPropagation();

    try {
      const token = localStorage.getItem("jwt");
      await axios.delete(`http://43.201.103.60:8080/post/${id}`, {
        headers: {
          Authorization: ` ${token}`, // 토큰 추가
        },
      });

      // 로컬 상태에서 해당 일기 제거
      setDiaries((prevDiaries) =>
        prevDiaries.filter((diary) => diary.id !== id)
      );
      alert("일기가 성공적으로 삭제되었습니다.");
    } catch (error) {
      console.error("일기 삭제 실패:", error.response?.data || error.message);
      alert("일기를 삭제하는 데 실패했습니다.");
    }
  };

  const displayedDiaries = isReversed ? [...diaries].reverse() : diaries;

  useEffect(() => {
    const fetchDiaries = async () => {
      try {
        const token = localStorage.getItem("jwt"); // JWT 토큰 가져오기
        const response = await axios.get("http://43.201.103.60:8080/post", {
          headers: {
            Authorization: ` ${token}`, // 토큰 추가
          },
        });
        setDiaries(response.data); // 서버에서 받은 일기 목록으로 상태 업데이트
      } catch (error) {
        console.error("일기 목록을 가져오는 데 실패했습니다:", error);
      }
    };

    fetchDiaries();
  }, []);
  // diaries 상태가 업데이트될 때마다 로컬 스토리지에 저장

  useEffect(() => {
    if (state?.newDiary) {
      setDiaries((prevDiaries) => {
        const isDuplicate = prevDiaries.some(
          (diary) => diary.id === state.newDiary.id
        );
        if (isDuplicate) {
          return [...prevDiaries, state.newDiary];
        }
        return prevDiaries; // 중복 방지
        // 기존 일기에 새 일기 추가
      });
      // 상태를 초기화하지 않음으로써 기존 일기가 사라지는 문제를 방지
    }
  }, [state?.newDiary]);
  return (
    <>
      <GlobalStyle />
      <AppContainer>
        {/* Title */}
        <Header>오늘의 일기</Header>

        {/* List */}
        <ListContainer>
          <Table>
            <thead>
              <tr>
                <Th>목록</Th>
                <Th>날짜</Th>
                <Th>일기</Th>
                <Th>삭제</Th>
              </tr>
            </thead>
            <tbody>
              {displayedDiaries.map((diary, index) => (
                <Tr
                  key={diary.id || index}
                  onClick={() => handleDiaryClick(diary)}
                >
                  <Td>{index + 1}</Td>
                  <Td>{diary.createdDate || "NoDate"}</Td>
                  <Td>{diary.content || "Untitled"}</Td>
                  <Td>
                    <DeleteButton
                      onClick={(event) => handleDeleteDiary(diary.id, event)}
                    >
                      삭제
                    </DeleteButton>
                  </Td>
                </Tr>
              ))}
            </tbody>
          </Table>
        </ListContainer>

        <WriteButton onClick={GoCalender}>일기쓰기</WriteButton>
      </AppContainer>
    </>
  );
}

export default ThirdPage;

// 스타일 정의
const GlobalStyle = createGlobalStyle`
  body {
    margin: 0;
    padding: 0;
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    width: 100vw;
    background-color: #f5f5f5; /* 회색 배경 */
    font-family: "Arial", sans-serif; /* 모던한 폰트 */
  }
`;

const AppContainer = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 600px;
  text-align: center;
  padding: 20px;
  border-radius: 15px;
  background-color: #ffffff; /* 흰색 카드 */
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1); /* 그림자 효과 */
`;

const Header = styled.header`
  font-size: 24px;
  font-weight: bold;
  color: #333333;
  margin-bottom: 20px;
`;

const ListContainer = styled.div`
  margin-top: 20px;
  width: 100%;
`;

const Table = styled.table`
  width: 100%;
  border-collapse: collapse;
`;

const Th = styled.th`
  background-color: #e0e0e0;
  color: #333333;
  padding: 12px;
  font-size: 18px;
  border-bottom: 2px solid #cccccc;
`;

const Tr = styled.tr`
  cursor: pointer;
  &:hover {
    background-color: #f9f9f9;
  }
`;

const Td = styled.td`
  padding: 12px;
  border: 1px solid #dddddd;
  text-align: center;
  color: #555555;
`;

const WriteButton = styled.button`
  margin-top: 20px;
  padding: 10px 20px;
  border-radius: 8px;
  border: none;
  background-color: #6c757d;
  color: white;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  &:hover {
    background-color: #0056b3;
  }
`;
const DeleteButton = styled.button`
  padding: 5px 10px;
  background-color: #ff4d4f;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;

  &:hover {
    background-color: #d9363e;
  }
`;
