import React, { useCallback, useState } from 'react';
import { Drawer, Box, Stack, IconButton, Typography } from '@mui/material';
import { Chat as ChatIcon } from '@mui/icons-material';
import DisabledByDefaultIcon from '@mui/icons-material/DisabledByDefault';
import Message from './Message';
import { useEffect } from 'react';
import { User } from './UserContext';

const ChatDrawer = () => {
  const { isOpen, setIsOpen } = useState(false);

  const handleClick = () => {
    setIsOpen(true);
  };
  const closeDrawer = () => {
    setIsOpen(false);
  };
  const { userId } = User();
  const [sent, setMessageState] = useState(false);
  const { userToken } = User();
  const { SERVER_URL } = User();
  const [messages, setMessages] = useState([]);

  const fetchMessages = useCallback(() => {
    fetch(`${SERVER_URL}/api/chat/${userId}`, {
      method: 'GET',
      headers: {
        Authorization: `Bearer ${userToken}`,
      },
    })
      .then((response) => response.json())
      .then((messages) => setMessages((messages)));
  }, [userToken]);

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
        sx={{ display: { xs: 'none', md: 'flex' }, color: 'black' }}
        anchor="right"
        open={isOpen}
        onClose={closeDrawer}
      >
        <Stack spacing={2} sx={{ width: '35vw', height: '100vh', display: 'flex', alignItems: 'center', bgcolor: '#fffef7' }}>
          <Box id="header" sx={{ height: '15%', width: '35vw', bgcolor: '#ffffed', justifyContent: 'space-between', display: 'flex', flexDirection: 'row', alignItems: 'center' }}>
            <Box sx={{ display: 'flex', flexDirection: 'row', justifyContent: 'space-around', paddingLeft: '5%', alignItems: 'center', width: '15vw', height: '100%' }}>
              <Box sx={{ height: '50%', width: '20%', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
                <ChatIcon sx={{ height: '60%', color: 'black' }} />
              </Box>
              <Typography variant="body1" class="header_details">
                CHAT
              </Typography>
            </Box>
            <IconButton onClick={closeDrawer} sx={{ height: '40%', borderRadius: '0', width: '10%', marginRight: '6%', display: 'flex' }}>
              <DisabledByDefaultIcon sx={{ color: 'black' }} />
            </IconButton>
          </Box>

          <Box id="messages" sx={{ height: '75%', width: '100%', bgcolor: '#fffef7', flex: '1', overflowY: 'auto' }}>
            {messages.map((m) => {
              const sent = m.recipient_id !== userId;
              const cName = sent ? 'sentMessage' : 'receivedMessage';
              return <Message key={m.id} cName={cName} message={m.message} />;
            })}
          </Box>
          <button id="send">Send</button>
        </Stack>
      </Drawer>
    </Box>
  );
};

export default ChatDrawer;
