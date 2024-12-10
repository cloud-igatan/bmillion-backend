import React, { Children } from "react";
import styled from "styled-components";


function Button({ onClick }) {
  return <StyledButton onClick={onClick}>{Children}</StyledButton>;
}



export default Button;

const StyledButton = styled.button`
  width: 200px;  /* 버튼 너비 조정 */
  height: 50px;
  border: none;
  border-radius: 10px;
  background-color: #ffffff;
  color: black;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  margin-left: 110px; 
  margin-right: 110px;
`;
