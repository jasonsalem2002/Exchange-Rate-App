
import React, { useCallback, useEffect } from 'react'
import { useState } from 'react';
import { Alert, AppBar, Box, Button, Link, MenuItem, Select, Snackbar, TextField, Toolbar, Typography } from '@mui/material';

import { User } from '../UserContext';
import DrawerNav from '../DrawerNav';
import './Message.css'


function Message({ cName, content,addedDate, sent }) {
  return (

      <Box className={cName}>
      <Typography className={cName+'Text'}>{content}</Typography>
      <Typography>{addedDate}</Typography>
      </Box>
  );
}

export default Message;
