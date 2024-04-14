import React, { useCallback, useState } from 'react';
import { Drawer, Box, Stack, IconButton, Typography,TextField,Button } from '@mui/material';
import { Chat as ChatIcon } from '@mui/icons-material';
import DisabledByDefaultIcon from '@mui/icons-material/DisabledByDefault';
import Message from './Message';
import { useEffect } from 'react';
import { User } from './UserContext';
import UserChatBox from './UserChatBox';
import ChatUser from './ChatUser';
import ArrowBackIosIcon from '@mui/icons-material/ArrowBackIos';
import AddCircleIcon from '@mui/icons-material/AddCircle';
import Dialog from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import './App.css'

const ChatDrawer = () => {


  const [isOpen,setIsOpen] = useState(false);
  const  username=localStorage.getItem('username')
  const[dialogOpen,setDialogOpen]=useState(false)
  const[activeUsers,setActiveUsers]=useState([])
  const[inactiveUsers,setInActiveUsers]=useState([])
  const [lastMessages,setLastMessages]=useState([])
  const {fetchMessages}=User()
  const {fetchUsernames}=User()
  const {usernames}=User()
  const {messages}=User()

  const handleClick = () => {
    setIsOpen(true);
  };

  const closeDrawer = () => {
    setIsOpen(false);
  };

  const addChat=(name)=>
  {
    setDialogOpen(false);
    setChatState(true)
    setChatName(name)
    setActiveUsers([...activeUsers, name])
  }


  const [sent, setMessageState] = useState(false);
  const { userToken } = User();
  const { SERVER_URL } = User();

  const [message,setMessage]= useState('')
  const[chatName,setChatName]=useState(null)
  const [chatOpened,setChatState]=useState(false)

  const usernamesExceptCurrent= usernames.filter(u => u !== username)
 

  const openChat=(name) =>
  {
    setChatState(true)
    setChatName(name)
  }
  
 

useEffect(()=>{fetchUsernames();fetchMessages()},[])



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
          content: lastMessage.content,
          added_date: lastMessage.added_date
        };
    
    });
  
    console.log('here',lastm)
   
    setLastMessages(lastm);
    
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


    console.log(filtered)
    setActiveUsers(filtered);
    
    // localStorage.setItem('activeUsers', JSON.stringify(filtered)); 

    const inactive = usernamesExceptCurrent.filter(u => !filtered.includes(u));
    setInActiveUsers(inactive);
    getLastMessages()

    // if (lastMessages.length!==0)
    // {
       
    // }

  }, [username,usernames,messages]);
  

  useEffect(() => {

   sortUsers(lastMessages)

  },[lastMessages]);
  
  


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

        <Stack  sx={{ width: '55vw', height: '100%', display: 'flex', alignItems: 'center', backgroundColor: 'white' }}>
          <Box id="header" sx={{ height: '20%', width: '55vw', bgcolor: '#0093d5', justifyContent: 'space-between', display: 'flex', flexDirection: 'row', alignItems: 'center' }}>
            <Box sx={{ display: 'flex', flexDirection: 'row', justifyContent: 'space-around', paddingLeft: '5%', alignItems: 'center', width: '15vw', height: '100%' }}>
            {chatOpened && <IconButton onClick={()=>setChatState(false)} sx={{ height: '40%', borderRadius: '0', width: '10%', marginRight: '6%', display: 'flex' }}>
              <ArrowBackIosIcon sx={{ color: 'white' }} />
            </IconButton>
            }

              
              <Box sx={{ height: '50%', width: '20%', display: 'flex', justifyContent: 'center', alignItems: 'center',paddingRight:'55%' }}>
                <ChatIcon sx={{ height: '80%', color: 'white' }} />
              </Box>
              <Typography variant='h4' sx={{color:'white'}}>
                CHAT
              </Typography>
            </Box>
            {!chatOpened&&
            <IconButton onClick={()=>{setDialogOpen(true)}}><AddCircleIcon sx={{color:'white'}}/></IconButton>
}
          </Box>

          <Dialog open={dialogOpen} fullWidth>
           
           <Box className='headerDrawer'>
            <DialogTitle className='headerText'>Add Chat</DialogTitle>
            </Box>
            {inactiveUsers.length===0 && <Box  sx={{display:'flex',flexDirection:'column',justifyContent:'space-around',alignItems:'center',padding:'10%'}}><Typography>You have a chat with all current users.</Typography>
            <Button className='formButton' onClick={()=>{setDialogOpen(false)}}>Ok</Button>            
            </Box>}


            <Box>
              
            {inactiveUsers.map((u) => {
        
            return <UserChatBox key={u.id} username={u} openChat={addChat} lastMessage={{content:'',added_date:''}} />;
            })}
            </Box>

            </Dialog>

        {!chatOpened &&
         <Box id="messages" sx={{  width: '100%', bgcolor: '#fffef7', flex: '1', overflowY: 'auto' }}>
        {activeUsers.map((u) => {
        
          return <UserChatBox key={u.id} username={u} openChat={openChat} lastMessage={{content: lastMessages[u]?.content || '', added_date: lastMessages[u]?.added_date || '' }} />;
        })}
      </Box>
}



{chatOpened && <ChatUser user={chatName} />}

          <Box sx={{width:'100%',height:'20%',display:'flex',flexDirection:'row',justifyContent:'space-around',backgroundColor:'#0093d5'}}>
         
          </Box>
        </Stack>
      </Drawer>
    </Box>
  );
};

export default ChatDrawer;
