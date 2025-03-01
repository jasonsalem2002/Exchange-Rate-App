import React, { useCallback, useState } from 'react';
import { Drawer, Box, Stack, IconButton, Typography,TextField,Button ,Input} from '@mui/material';
import { Chat as ChatIcon } from '@mui/icons-material';
import DisabledByDefaultIcon from '@mui/icons-material/DisabledByDefault';
import Message from './Message';
import { useEffect } from 'react';
import { User } from '../UserContext';
import UserChatBox from './UserChatBox';
import '../App.css'
import './ChatUser.css'

const ChatUser = ({user}) => {

  
  const username = localStorage.getItem('username')
  const [sent, setMessageState] = useState(false);
  const { userToken } = User();
  const { SERVER_URL } = User();
  const [message,setMessage]= useState('')
  const {fetchMessages}=User()
  const {fetchUsernames}=User()
  const {usernames}=User()
  const {messages}=User()
  
let messagesWithUser = messages.filter(m => m.recipient_username===user ||m.sender_username ===user);
 

// useEffect(fetchMessages,[])

  const sendMessage= useCallback((user)=>{ 
    setMessage('')
    console.log(user)
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
        "recipient_username":user,
        "content":message

      })
    })
    .then(response => {
      return response.json();
    })
    
    .then((body) => {
      fetchMessages()
  });
  })




  return (
    <Box sx={{ display: 'flex',height:'100%',flexDirection:'column', width: '100%' }}>
    
         <Box id="messages" sx={{ height: '80%', width: '100%',display:'flex',flexDirection:'column',bgcolor: '#fffef7', overflowY: 'auto' }}>
              {messagesWithUser.map((m) => {
                const sent = m.recipient_username !== username;
                const cName = sent ? 'sentMessage' : 'receivedMessage';
                return <Message key={m.id} cName={cName} content={m.content} addedDate={m.added_date} />;
              })}
        </Box>

          <Box sx={{width:'100%',height:'20%',display:'flex',flexDirection:'row',alignItems:'center',justifyContent:'space-around',backgroundColor:'lightGray'}}>
          <input
                id='sendField'
                type="text"
                value={message}
                onChange={(e) => setMessage(e.target.value)}
                onKeyPress={(event)=>{if (event.key === 'Enter') {
                  sendMessage(user);
                  setMessage('');
                }}}
              />

<Button id='sendButton' class='formButton' onClick={() => {sendMessage(user);setMessage('');}} disabled={message.trim().length===0}>Send</Button>

          </Box>
 
 
    </Box>
  );
};



export default ChatUser;
