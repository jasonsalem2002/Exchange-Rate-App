import React, { useCallback, useState } from 'react';
import { Drawer, Box, Stack, IconButton, Typography,TextField,Button ,Input} from '@mui/material';
import { Chat as ChatIcon } from '@mui/icons-material';
import DisabledByDefaultIcon from '@mui/icons-material/DisabledByDefault';
import Message from './Message';
import { useEffect } from 'react';
import { User } from './UserContext';
import UserChatBox from './UserChatBox';
import './App.css'
import GroupMessage from './GroupMessage';


const ChatGroup= ({group ,leaveGroup}) => {

  
  const username = localStorage.getItem('username')
  const [sent, setMessageState] = useState(false);
  const { userToken } = User();
  const { SERVER_URL } = User();
  const [message,setMessage]= useState('')
  const {groupsMessages}=User([])
  const {fetchUsernames}=User()
  const {usernames}=User()
  const {messages}=User()
  const {fetchJoinedGroups}=User()
 const {fetchGroupMessages}=User()
  const {sendGroupMessage}=User()



  useEffect(()=>{console.log(groupsMessages);fetchGroupMessages(group)},[])

 const groupm= groupsMessages[group] || ['']


  return (
    <Box sx={{ display: 'flex',height:'700px',flexDirection:'column', width: '100%' }}>
        <Box><Button sx={{backgroundColor:'green',color:'white'}} onClick={()=>leaveGroup(group)}>Leave</Button></Box>
         <Box id="messages" sx={{ height: '80%', width: '100%',display:'flex',flexDirection:'column',bgcolor: '#fffef7', overflowY: 'auto' }}>
              {groupm.map((m) => {
                const sent = m.sender_username === username;
                const cName = sent ? 'sentMessage' : 'receivedMessage';
                return <GroupMessage key={m.id} sender={m.sender_username} cName={cName} content={m.content} addedDate={m.added_date} />;
              })}
        </Box>

          <Box sx={{width:'100%',height:'20%',display:'flex',flexDirection:'row',alignItems:'center',justifyContent:'space-around',backgroundColor:'lightGray'}}>
          <input
                id='sendField'
                type="text"
                value={message}
                onChange={(e) => setMessage(e.target.value)}
              />

<Button id='sendButton' class='formButton' onClick={() => sendGroupMessage(group,message)}>Send</Button>

          </Box>
 
 
    </Box>
  );
};



export default ChatGroup;
