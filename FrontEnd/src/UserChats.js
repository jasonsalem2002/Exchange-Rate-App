import React, { useCallback, useState } from 'react';
import { Drawer, Box, Stack, IconButton, Typography,TextField,Button ,Input,Dialog,DialogTitle} from '@mui/material';
import { Chat as ChatIcon } from '@mui/icons-material';
import DisabledByDefaultIcon from '@mui/icons-material/DisabledByDefault';
import Message from './Message';
import { useEffect } from 'react';
import { User } from './UserContext';
import UserChatBox from './UserChatBox';
import ChatUser from './ChatUser';
import './App.css';


const UserChats = ({activeUsers,setActiveUsers,setDialogOpen,dialogOpen,userChatOpened,setUserChatState}) => {
  
  const username = localStorage.getItem('username')
  const[inactiveUsers,setInActiveUsers]=useState([])
  const [lastMessages,setLastMessages]=useState([])
  const { userToken } = User();
  const { SERVER_URL } = User();
  const [message,setMessage]= useState('')
  const {fetchMessages}=User()
  const {fetchUsernames}=User()
  const {usernames}=User()
  const {messages}=User()
  const usernamesExceptCurrent= usernames.filter(u => u !== username)
  const[chatName,setChatName]=useState(null)

  const openUserChat=(name) =>
  {
    setDialogOpen(false)
    setUserChatState(true)
    setChatName(name)
  }

  const addUserChat=(name)=>
  {
    setDialogOpen(false);
    setUserChatState(true)
    setChatName(name)
    setActiveUsers([...activeUsers, name])
  }

  


// let messagesWithUser = messages.filter(m => m.recipient_username===user ||m.sender_username ===user);



    function getLastMessages() {

    const lastm = {};
    activeUsers.forEach(activeUser => {
      const messagesWithActiveUser = messages.filter(message =>
        (message.sender_username === activeUser && message.recipient_username === username) ||
        (message.sender_username === username && message.recipient_username === activeUser)
      );

        const lastMessage = messagesWithActiveUser[messagesWithActiveUser.length - 1];

        // console.log(lastMessage)
        lastm[activeUser] = {
          content: lastMessage?.content||'',
          added_date: lastMessage?.added_date||''
        };
    
    });
  
    console.log('here',lastm)
   
    setLastMessages(lastm);
    
  }

  function sortUsers(lastm) {

    const sortedActiveUsers = [...activeUsers].sort((userA, userB) => {
      const latestMessageDateA = new Date(lastm[userA]?.added_date||'');
      console.log("USER A",latestMessageDateA)
      const latestMessageDateB = new Date (lastm[userB]?.added_date||'');
      console.log("USER B",latestMessageDateB)
      return latestMessageDateB - latestMessageDateA; // Descending order
    });
  
  console.log("SORTED:",sortedActiveUsers)
    setActiveUsers(sortedActiveUsers);
    
  
  }


  
  useEffect(() => {

    const filtered = usernamesExceptCurrent.filter(u => {
      return messages.some(m => {
        return (
          (m.sender_username === u && m.recipient_username === username) ||
          (m.sender_username === username && m.recipient_username === u)
        );
      });
    });

    setActiveUsers(filtered);
    
    localStorage.setItem('activeUsers', JSON.stringify(filtered)); 

    const inactive = usernamesExceptCurrent.filter(u => !filtered.includes(u));
    setInActiveUsers(inactive);
    getLastMessages()

   

  }, [username,usernames,messages]);
  

  useEffect(() => {

   sortUsers(lastMessages)

  },[lastMessages]);




  useEffect(()=>{fetchUsernames();fetchMessages()},[])

  return (
    <Box sx={{ display: 'flex',height:'100%',flexDirection:'column', width: '100%' }}>
      <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)}  fullWidth>
           
           <Box className='headerDrawer'>
            <DialogTitle className='headerText'>Add Chat</DialogTitle>
            </Box>
            {inactiveUsers.length===0 && <Box  sx={{display:'flex',flexDirection:'column',justifyContent:'space-around',alignItems:'center',padding:'10%'}}><Typography>You have a chat with all current users.</Typography>
            <Button class='formButton' onClick={()=>{setDialogOpen(false)}}>Ok</Button>            
            </Box>}


            <Box>
              
            {inactiveUsers.map((user) => {
        
        
          return <UserChatBox key={user.id} username={user} openUserChat={addUserChat} lastMessage={{ content: lastMessages[user]?.content || '', added_date: lastMessages[user]?.added_date || '' }} />;
        }
      )}
            </Box>

            </Dialog>

        {!(userChatOpened) &&
         <Box id="messages" sx={{  width: '100%', bgcolor: '#fffef7', flex: '1', overflowY: 'auto' }}>
        {activeUsers.map((user) => {
        
          
          return <UserChatBox key={user.id} username={user} openUserChat={openUserChat} lastMessage={{ content: lastMessages[user]?.content || '', added_date: lastMessages[user]?.added_date || '' }} />;
        }
      )}
      </Box>
}



{userChatOpened && <ChatUser user={chatName} />}
 
 
    </Box>
  );
};



export default UserChats;
