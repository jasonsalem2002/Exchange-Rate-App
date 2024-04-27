import logo from './logo.svg';
import './App.css';
import React, { useCallback, useEffect } from 'react'
import { useState } from 'react';
import { Alert, AppBar, Box, Button, Dialog, DialogActions, DialogContent, DialogTitle, Link, MenuItem, Select, Snackbar, TextField, Toolbar, Typography } from '@mui/material';
import UserCredentialsDialog from './UserCredentialsDialog/UserCredentialsDialog';
import { getUserToken,saveUserToken, clearUserToken } from "./localstorage";
import Nav from './Nav';
import { User } from './UserContext';
import { DataGrid } from '@mui/x-data-grid';
import CreateOffer from './CreateOffer';



function AvailableOffers() {
  

  const {userToken}= User();
  const {SERVER_URL}= User();
 const username=localStorage.getItem('username')
  const [offers, setOffers] = useState([]);
 const {setUserChatState}=User();
 const {setChatName}=User()
 const {setIsDrawerOpen}=User();
 
  const markComplete = () => {
    setDialogOpen(false);
    fetch(`${SERVER_URL}/accept_offer/${selectedRow.id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${userToken}`,
      },
    })
    .then(response => {
      return response.json();
    })

    .then(body => {
      fetchOffers()
    })
  
  
  };
  

  const handleChat=()=>{
    console.log('here')
    setDialogOpen(false)
    setIsDrawerOpen(true)
    setUserChatState(true)
    setChatName(selectedRow.username)
  }

  const fetchOffers = useCallback(() => {
    fetch(`${SERVER_URL}/offers`, {
        method:'GET',
        headers: {
            Authorization: `Bearer ${userToken}`,
        },
    })
    .then((response) => response.json())
    .then((offers) => setOffers(offers));
}, [SERVER_URL, userToken,markComplete]);

const allOtherOffers = offers.filter(o => o.username !==username );

useEffect(fetchOffers,[])


// useEffect(() => {
//   if (userToken) {
//       fetchOffers();
//   }
// }, [fetchOffers, userToken]);


    const [selectedRow, setSelectedRow] = useState(null);
    const [dialogOpen, setDialogOpen] = useState(false);

    
    const handleRowClick = (params) => {
        setSelectedRow(params.row);
        setDialogOpen(true);
      };

    
    
  return (
    <div>
        <Nav/>
        
        <Box sx={{paddingTop:'10%', width:'100%',height:'600px',display:'flex',flexDirection:'column',alignItems:'center', justifyContent:'space-around'}}>
             <Typography variant='h5' sx={{height:'100px'}}>OFFERS</Typography> 
            <CreateOffer fetchOffers={fetchOffers}/>
        <DataGrid sx={{marginTop:'5%',maxWidth:'100%'}}
              columns={[
                { field: 'id', headerName: 'ID' },
                { field: 'username', headerName: 'Username' },
                { field: 'amount_requested', headerName: 'Amount Offered' },
                { field: 'amount_to_trade', headerName: 'Amount Requested' },
                { field: 'exchangeRate', headerName: 'Exchange Rate' },
                { field: 'added_date', headerName: 'Date' },
              ]}
              rows={allOtherOffers}
              onRowClick={handleRowClick}
            
            />
             </Box>

             <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)}>

        <DialogActions sx={{display:'flex',flexDirection:'column'}}>
          <Button onClick={markComplete} >
            Accept
          </Button>
          <Button onClick={handleChat} >
            Chat
          </Button>
        </DialogActions>
      </Dialog> 
    </div>
  );
}

export default  AvailableOffers;
