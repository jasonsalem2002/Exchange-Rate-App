import logo from './logo.svg';
import './Nav.css';
import React, { useCallback, useEffect } from 'react'
import { useState } from 'react';
import { Alert, AppBar, Box, Button, Link, MenuItem, Select, Snackbar, TextField, Toolbar, Typography } from '@mui/material';
import UserCredentialsDialog from './UserCredentialsDialog/UserCredentialsDialog';
import { getUserToken,saveUserToken, clearUserToken } from "./localstorage";
import { User } from './UserContext';
import DrawerNav from './DrawerNav';
import './Message.css'

function Message(cName, message) {

   

  return (

    <Box className={cName}>
        <Typography className='messageText'>{message}</Typography>    
    </Box>
  );
}

export default Message;
