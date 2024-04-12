import React, { useCallback, useState } from 'react';
import { Drawer, Box, Stack, IconButton, Typography,TextField } from '@mui/material';
import { Chat as ChatIcon } from '@mui/icons-material';
import DisabledByDefaultIcon from '@mui/icons-material/DisabledByDefault';
import Message from './Message';
import { useEffect } from 'react';
import { User } from './UserContext';

const ChatDrawer = () => {


  const [isOpen,setIsOpen] = useState(false);
  const [usernames,setUserNames]= useState([])
  const username = localStorage.getItem('username')

  const handleClick = () => {
    setIsOpen(true);
  };

  const closeDrawer = () => {
    setIsOpen(false);
  };


  const [sent, setMessageState] = useState(false);
  const { userToken } = User();
  const { SERVER_URL } = User();
  const [messages, setMessages] = useState([]);
  const [message,setMessage]= useState('')

  const fetchUsernames = () => {
    console.log(username)
    fetch(`${SERVER_URL}/usernames`, {
        method:'GET',
        headers: {
            Authorization: `Bearer ${userToken}`,
        },
    })
    .then((response) => response.json())
    .then((usernames) => setUserNames(usernames));
}

  const fetchMessages = () => {
    fetch(`${SERVER_URL}/chat/${username}`, {
      method: 'GET',
      headers: {
        Authorization: `Bearer ${userToken}`,
      },
    })
      .then((response) => response.json())
      .then((messages) => setMessages((messages)));
  }


  function sendMessage() {
    setMessage('')
    fetchUsernames()
    fetchMessages()
    console.log(usernames)
    if (message.length<1)
    {
      return
    }
  
    fetch(`${SERVER_URL}/chat`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${userToken}`,
      },
      body: JSON.stringify({
        "recipient_username":'kareem',
        "content":message

      })
    })
    .then(response => {
      return response.json();
    })
  }  

  return (
    <Box sx={{ display: 'flex', height: '100%', width: '20%', justifyContent: 'center', alignItems: 'center' }}>
      {userToken && (
        <IconButton
          size="large"
          variant="contained"
          onClick={handleClick}
          sx={{ height: '30%', width: '15%', '&:hover': { opacity: 0.6, transition: 'opacity .25s ease-in-out' } }}
        >
          <ChatIcon sx={{ color: 'white' }} />
        </IconButton>
      )}

      <Drawer
        anchor="left"
        open={isOpen}
        onClose={closeDrawer}
      >

        <Stack spacing={2} sx={{ width: '35vw', height: '100vh', display: 'flex', alignItems: 'center', backgroundColor: 'white' }}>
          <Box id="header" sx={{ height: '15%', width: '35vw', bgcolor: '#0093d5', justifyContent: 'space-between', display: 'flex', flexDirection: 'row', alignItems: 'center' }}>
            <Box sx={{ display: 'flex', flexDirection: 'row', justifyContent: 'space-around', paddingLeft: '5%', alignItems: 'center', width: '15vw', height: '100%' }}>
              <Box sx={{ height: '50%', width: '20%', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
                <ChatIcon sx={{ height: '80%', color: 'white' }} />
              </Box>
              <Typography variant='h4' sx={{color:'white'}}>
                CHAT
              </Typography>
            </Box>
            <IconButton onClick={closeDrawer} sx={{ height: '40%', borderRadius: '0', width: '10%', marginRight: '6%', display: 'flex' }}>
              <DisabledByDefaultIcon sx={{ color: 'white' }} />
            </IconButton>
          </Box>

         <Box id="messages" sx={{ height: '50%', width: '100%', bgcolor: '#fffef7', flex: '1', overflowY: 'auto' }}>
  {messages.map((m) => {
    const sent = m.recipient_username !== username;
    const cName = sent ? 'sentMessage' : 'receivedMessage';
    return <Message key={m.id} cName={cName} message={m.content} />;
  })}
</Box>

          <Box sx={{width:'100%',height:'20%',display:'flex',flexDirection:'row',justifyContent:'space-around',backgroundColor:'#0093d5'}}>
          <TextField
                className='formField'
                type="text"
                value={message}
                onChange={(e) => setMessage(e.target.value)}
              />

          <button id="send" onClick={sendMessage }>Send</button>
          </Box>
        </Stack>
      </Drawer>
    </Box>
  );
};

export default ChatDrawer;
