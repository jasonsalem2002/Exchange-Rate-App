import logo from './logo.svg';
import './UserChatBox.css';
import './App.css';
import React, { useCallback, useEffect } from 'react'
import { useState } from 'react';
import { Alert, AppBar, Box, Button, Link, MenuItem, Select, Snackbar, TextField, Toolbar, Typography ,IconButton} from '@mui/material';
import UserCredentialsDialog from './UserCredentialsDialog/UserCredentialsDialog';
import { getUserToken,saveUserToken, clearUserToken } from "./localstorage";
import { User } from './UserContext';
import Person2Icon from '@mui/icons-material/Person2';



function UserChatBox({ username, lastMessage,openChat }) {
  
  const truncateText = (text, maxLength) => {
    if (text.length > maxLength) {
        return `${text.substring(0, maxLength)} ...`;
    }
    return text;
};

  return (
    <Box onClick={()=>{openChat(username)}} sx={{height:'100px',display:'flex',flexDirection:'row',justifyContent:'space-around',borderBottom:'2px solid lightGray'}}>
      <Box sx={{width:'20%',display:'flex',flexDirection:'column',justifyContent:'center',alignItems:'center'}}> 
        <IconButton sx={{height:'20%',width:'20%'}}><Person2Icon sx={{fontSize:'30px'}} /></IconButton>
      </Box>
      <Box sx={{width:'80%',display:'flex',flexDirection:'column',justifyContent:'center'}}>
        <Box sx={{height:'60%'}}>
        <Box sx={{height:'40%',width:'70%',display:'flex',flexDirection:'row',justifyContent:'space-between'}}>
        <Box sx={{width:'30%'}}>
            <Typography id='user'>{username}</Typography>  
        </Box>  
        
          <Typography id='date'>{lastMessage.added_date}</Typography>
    
        </Box>
 
            <Typography id='lmessage'className={lastMessage.content.length > 12 ? 'full' : ''}>{truncateText(lastMessage.content, 12)}</Typography>
   
        </Box>
        </Box>
    </Box>
  );
}



export default UserChatBox;




