import React, { useCallback, useState } from 'react';
import { Drawer, Box, Stack, IconButton, Typography,TextField,Button,Tabs,Tab} from '@mui/material';
import { Chat as ChatIcon } from '@mui/icons-material';
import DisabledByDefaultIcon from '@mui/icons-material/DisabledByDefault';
import Message from './Message';
import { useEffect } from 'react';

import UserChatBox from './UserChatBox';
import ChatUser from './ChatUser';
import ChatGroup from './ChatGroup';
import ArrowBackIosIcon from '@mui/icons-material/ArrowBackIos';
import AddCircleIcon from '@mui/icons-material/AddCircle';
import Dialog from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import '../App.css'
import './ChatDrawer.css'
import GroupChatBox from './GroupChatBox';
import UserChats from './UserChats';
import GroupChats from './GroupChats';
import { User } from '../UserContext';

const ChatDrawer = () => {


 const {isDrawerOpen}=User();
 const {setIsDrawerOpen}=User()
  const  username=localStorage.getItem('username')
  const[dialogOpen,setDialogOpen]=useState(false)
  const {fetchMessages}=User()
  const {fetchUsernames}=User()
  const {fetchJoinedGroups}=User()
  const {usernames}=User()
  const {messages}=User()
  const[activeUsers,setActiveUsers]=useState([])
  const [typeOfChats,setTypeOfChats]=useState('users')
  const {userChatOpened}=User()
  const {setUserChatState}=User();
  const {leaveGroup}=User()
 const {chatName}=User();
 const {setChatName}=User()
  const {joinGroup}=User()



  const handleClick = () => {
    setIsDrawerOpen(true);
  };

  const closeDrawer = () => {
    setIsDrawerOpen(false);
  };





  const [sent, setMessageState] = useState(false);
  const { userToken } = User();
  const { SERVER_URL } = User();

  const [message,setMessage]= useState('')
  const {groupChatOpened}=User()
  const {setGroupChatState}=User()

 
 


//   useEffect(() => {


//    console.log(username)
//     const filtered = usernamesExceptCurrent.filter(u => {
//       return messages.some(m => {
//         return (
//           (m.sender_username === u && m.recipient_username === username) ||
//           (m.sender_username === username && m.recipient_username === u)
//         );
//       });
//     });


//     console.log(filtered)
//     setActiveUsers(filtered);
    
//     localStorage.setItem('activeUsers', JSON.stringify(filtered)); 

//     // const inactive = usernamesExceptCurrent.filter(u => !filtered.includes(u));
//     // setInActiveUsers(inactive);
//     getLastMessages()

   

//   }, [username,usernames,messages]);
  

//   useEffect(() => {

//    sortUsers(lastMessages)

//   },[lastMessages]);


  


  return (
    <Box sx={{ display: 'flex', height: '100%', width: '20%', justifyContent: 'center', alignItems: 'center' }}>
      {userToken && (
        <IconButton
          size="large"
          variant="contained"
          onClick={handleClick}
          sx={{backgroundColor:'#0093d5' ,height: '30%', width: '30%', '&:hover': { opacity: 0.6, transition: 'opacity .25s ease-in-out' } }}
        >
          <ChatIcon sx={{backgroundColor:'#0093d5' ,color: 'white' }} />
        </IconButton>
      )}

      <Drawer 
        anchor="left"
        open={isDrawerOpen}
        onClose={closeDrawer}
      >
        
        <Stack id='stackDrawer'  sx={{ width: '55vw', height: '100%', display: 'flex', alignItems: 'center', backgroundColor: 'white' }}>
          <Box id="header" sx={{ height: '30%', width: '55vw', bgcolor: '#0093d5', justifyContent: 'space-between', display: 'flex', flexDirection: 'row', alignItems: 'center' }}>
            <Box sx={{ display: 'flex', flexDirection: 'row', justifyContent: 'space-around', paddingLeft: '5%', alignItems: 'center', width: '15vw', height: '100%' }}>
            {(userChatOpened|| groupChatOpened )&& <IconButton onClick={()=>{setGroupChatState(false);setUserChatState(false)}} sx={{ height: '40%', borderRadius: '0', width: '10%', marginRight: '6%', display: 'flex' }}>
              <ArrowBackIosIcon sx={{ color: 'white' }} />
            </IconButton>
            }

              
              <Box sx={{ height: '50%', width: '20%', display: 'flex', justifyContent: 'center', alignItems: 'center',marginRight:'20%' }}>
                <ChatIcon sx={{ height: '80%', color: 'white' }} />
              </Box>
              {!(userChatOpened || groupChatOpened) && <Typography  sx={{color:'white'}}>
                CHAT
              </Typography>
}
{(userChatOpened || groupChatOpened) && <Typography  sx={{color:'white'}}>
                {chatName}

             </Typography>}

             {groupChatOpened&& <Button sx={{marginLeft:'20%',backgroundColor:'green',color:'white'}} onClick={()=>leaveGroup(chatName)}>Leave</Button>}

             {groupChatOpened}
            </Box>
            {!(userChatOpened|| groupChatOpened )&&
            <IconButton sx={{color:'white',marginRight:'5%'}} onClick={()=>{setDialogOpen(true);}}><AddCircleIcon sx={{color:'white',marginRight:'5%'}}/></IconButton>
}
          </Box>
          {!(userChatOpened||groupChatOpened) &&
          <Box sx={{ width: '55vw' }}>
      <Tabs value={typeOfChats} onChange={(e,v)=>{setTypeOfChats(v)}}  >
        <Tab sx={{width:'50%'}} value='users' label="Users"  />
        <Tab sx={{width:'50%'}} value='groups'  label="Groups"  />
      </Tabs>
     
    </Box>
}

{typeOfChats==='users'&&<UserChats activeUsers={activeUsers} setActiveUsers={setActiveUsers} setDialogOpen={setDialogOpen} dialogOpen={dialogOpen} userChatOpened={userChatOpened} setUserChatState={setUserChatState} />}
{typeOfChats==='groups'&&<GroupChats setDialogOpen={setDialogOpen} dialogOpen={dialogOpen} groupChatOpened={groupChatOpened} setGroupChatState={setGroupChatState} />}

          <Box sx={{width:'100%',height:'20%',display:'flex',flexDirection:'row',justifyContent:'space-around',backgroundColor:'#0093d5'}}>
         
          </Box>
        </Stack>
      </Drawer>
    </Box>
  );
};

export default ChatDrawer;
