import React, { useCallback, useState } from 'react';
import { Drawer, Box, Stack, IconButton, Typography,TextField,Button ,Input,Dialog,DialogTitle,Snackbar,Alert} from '@mui/material';
import { Chat as ChatIcon } from '@mui/icons-material';
import DisabledByDefaultIcon from '@mui/icons-material/DisabledByDefault';
import Message from './Message';
import { useEffect } from 'react';
import { User } from '../UserContext';
import UserChatBox from './UserChatBox';
import ChatUser from './ChatUser';
import '../App.css';
import SearchIcon from '@mui/icons-material/Search';

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
  const{chatName}=User();
  const {setChatName}=User();
  const[addedUser,setAddedUser]=useState('')
  const [userAlreadyAdded,setUserAlreadyAdded]=useState(false)
  const [userDoesntExist,setUserDoesntExist]=useState(false)

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

  const handleAddUser=()=>
  {
    
    if (activeUsers.includes(addedUser))
    {
      setUserAlreadyAdded(true)
      
    }

    else if(!usernamesExceptCurrent.includes(addedUser))
    {
     
      setUserDoesntExist(true)
    }

    else 
    {
      addUserChat(addedUser)
    }
  }

  


// let messagesWithUser = messages.filter(m => m.recipient_username===user ||m.sender_username ===user);
//ass


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
           
           <Box className='headerDrawer' sx={{padding:'3%'}}>
          
            <Box sx={{display:'flex',flexDirection:'row',justifyContent:'space-around',alignItems:'center',width:'70%'}}> <TextField
            sx={{backgroundColor:'white'}}
            
      onChange={(e)=>setAddedUser(e.target.value)}
      variant="outlined"
      InputProps={{
        startAdornment: <SearchIcon sx={{marginRight:'3%'}} />,
      }}
      required
    />
    <Button class='formButton'  onClick={()=>{handleAddUser()}}>Add</Button>
    </Box>

            
            </Box>
            {inactiveUsers.length===0 && <Box  sx={{display:'flex',flexDirection:'column',justifyContent:'space-around',alignItems:'center',padding:'10%'}}><Typography>You have a chat with all current friends.</Typography>
            <Button class='formButton' style={{height:'35%'}} onClick={()=>{setDialogOpen(false)}}>Ok</Button> 
            <Snackbar elevation={6}variant="filled" open={userDoesntExist} autoHideDuration={2000} onClose={() => setUserDoesntExist(false)}>
        <Alert severity="error">Please enter an existing user</Alert>
    </Snackbar>
    <Snackbar elevation={6}variant="filled" open={userAlreadyAdded} autoHideDuration={2000} onClose={() => setUserAlreadyAdded(false)}>
        <Alert severity="error">User already added</Alert>
    </Snackbar>           
            </Box>}


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
