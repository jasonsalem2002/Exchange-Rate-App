import React, { useCallback, useState } from 'react';
import { Drawer, Box, Stack, IconButton, Typography,TextField,Button ,Input,Dialog,DialogTitle} from '@mui/material';
import { Chat as ChatIcon } from '@mui/icons-material';
import DisabledByDefaultIcon from '@mui/icons-material/DisabledByDefault';
import Message from './Message';
import { useEffect } from 'react';
import { User } from '../UserContext';
import GroupChatBox from './GroupChatBox';
import ChatGroup from './ChatGroup';
import '../App.css';




const GroupChats = ({setDialogOpen,dialogOpen,groupChatOpened,setGroupChatState}) => {
  
  const username = localStorage.getItem('username')
  const {fetchGroups}=User()
  const {joinedGroups}=User()
  const {groups}=User()
  const [lastMessages,setLastMessages]=useState([])
  const { userToken } = User();
  const { SERVER_URL } = User();
  const [message,setMessage]= useState('')
  const {fetchMessages}=User()
  const {fetchUsernames}=User()
  const {usernames}=User()
  const {messages}=User()
  const {fetchJoinedGroups}=User()
  const [inactiveGroups,setInActiveGroups]=useState([]);
  const [creatingGroup,setCreatingGroup]=useState(false)
  const [newGroupName,setNewGroupName]=useState('')
  const {chatName}=User();
  const {setChatName}=User()
  const [sortedGroups,setSortedGroups]=useState([])
    const {joinGroup}=User()
  const {createGroup}=User()
    const {fetchGroupMessages}=User();
    const {groupsMessages}=User();
    const {setJoinedGroups}=User();
  
    const addGroupChat=(name) =>
    {
    joinGroup(name)
      setDialogOpen(false)
      setGroupChatState(true)
      setChatName(name)
    }
  

  const openGroupChat=(name) =>
  {
    setDialogOpen(false)
    setGroupChatState(true)
    setChatName(name)
  }

  

  function getLastMessageOfGroups() {

    const lastm = {};
    console.log(joinedGroups)
    joinedGroups.forEach(group => {
        console.log('GROUP NAME:',group)
        console.log(groupsMessages)
        console.log('Messages of the group',groupsMessages[group])
        console.log('here')
        const groupm = groupsMessages[group] || 'g';
        
        const lastMessage = groupm[groupm.length - 1];

        // console.log(lastMessage)
        lastm[group] = {
          content: lastMessage?.content||'',
          added_date: lastMessage?.added_date||''
        };

        sortGroups(lastm)
        
    });
  
    console.log('here',lastm)
   
    setLastMessages(lastm);
    
  }



  function sortGroups(lastm) {
    const sortedGroups1 = [...joinedGroups].sort((groupA, groupB) => {
      
      const latestMessageDateA = lastm[groupA] ? new Date(lastm[groupA].added_date) : new Date(0);

      const latestMessageDateB = lastm[groupB] ? new Date(lastm[groupB].added_date) : new Date(0);
    
      return latestMessageDateB - latestMessageDateA;
    });
  
    setSortedGroups(sortedGroups1);
    console.log('SORTED GROUPS: ',sortedGroups1)
  }



useEffect(()=>{

    joinedGroups.forEach((group) => {
        console.log('here')
        fetchGroupMessages(group);
        
    });

    const inactive=  groups.filter(g => !joinedGroups.includes(g));
    setInActiveGroups(inactive);

},[joinedGroups,groups])


useEffect(()=>{
    
    fetchJoinedGroups();
    console.log('here1')

},[])





useEffect(()=>{console.log(groupsMessages)},[groupsMessages])


useEffect(getLastMessageOfGroups,[joinedGroups,groupsMessages])

  return (
    <Box sx={{ display: 'flex',height:'100%',flexDirection:'column', width: '100%' }}>
      <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)}  fullWidth>
           

           <Box className='headerDrawer'>
            <DialogTitle className='headerText'>Join Group 
            <Button  onClick={()=>{setDialogOpen(false);setCreatingGroup(true)}} sx={{backgroundColor:'green', color:'white'}}>Create Group</Button>
            </DialogTitle>
            
            </Box>
            {inactiveGroups.length===0 && <Box   sx={{display:'flex',flexDirection:'column',justifyContent:'space-around',alignItems:'center',padding:'10%'}}><Typography>There are no availible groups left to join.</Typography>
            <button class='formButton' onClick={()=>{setDialogOpen(false)}}>Ok</button>            
            </Box>}


            
            <Box>
              
            {inactiveGroups.map((group) => {
        
        
          return <GroupChatBox key={group.id} group={group} openGroupChat={addGroupChat} lastMessage={{ content: '', added_date:  '' }} />;
        }
      )}
            </Box>

            </Dialog>


            <Dialog open={creatingGroup} onClose={() => setCreatingGroup(false)}  fullWidth>
           
           <Box className='headerDrawer'>
            <DialogTitle  className='headerText'>Create Group </DialogTitle>
             
            <Box sx={{display:'flex',flexDirection:'column',alignItems:'center',backgroundColor:'transparent',width:'100%'}}>
            <TextField sx={{margin:'4%'}}  className='formField' label="Group Name"  value={newGroupName} onChange={e =>setNewGroupName(e.target.value)}/>
            <Button disabled={newGroupName.trim().length===0} sx={{margin:'2%',backgroundColor:'green',color:'white'}} className='formButton' onClick={()=>{setCreatingGroup(false);createGroup(newGroupName)}}>Create</Button>
            </Box>

            </Box>
            </Dialog>


        {!(groupChatOpened) &&
         <Box id="messages" sx={{  width: '100%', bgcolor: '#fffef7', flex: '1', overflowY: 'auto' }}>
        {sortedGroups.map((group) => {
        
          
          return <GroupChatBox group={group}  openGroupChat={openGroupChat} lastMessage={{ content: lastMessages[group]?.content || '', added_date: lastMessages[group]?.added_date }} />;
        }
      )}
      </Box>
}





{groupChatOpened && <ChatGroup group={chatName} />}
 
 
    </Box>
  );
};



export default GroupChats;
