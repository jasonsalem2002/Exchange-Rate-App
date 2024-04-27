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


function CreateOffer(fetchOffers) {

   const [dialogOpen, setDialogOpen] = useState(false);
   const [transactionType,setTransactionType]= useState(false);

   const [amountRequested,setAmountRequested]=  useState();
  const [amountToTrade,setAmountToTrade]= useState();

  const {SERVER_URL}= User();
  const {userToken}= User();

  function addOffer() {
    setDialogOpen(false)
    fetch(`${SERVER_URL}/offers`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${userToken}`,
      },
      body: JSON.stringify({
        "amount_requested": parseFloat(amountRequested),
        "amount_to_trade": parseFloat(amountToTrade),
        "usd_to_lbp": transactionType
      })
    })

    .then(response => {
      return response.json();
    })

    .then(body => {
      fetchOffers()
    })
  }  

   return(
   <div>
    <Button sx={{backgroundColor:'#0093d5',color:'white'}} onClick={()=>setDialogOpen(true)}>Create Offer</Button>
    <Dialog fullWidth open={dialogOpen} onClose={() => setDialogOpen(false)}>
    <Box sx={{backgroundColor:'#0093d5',height:'86px',display:'flex',flexDirection:'column',justifyContent:'center',paddingLeft:'4%'}}><Typography fontWeight={'bold'} color={'white'} variant='h6'>Create Offer</Typography></Box>
    <form style={{height:'500px',width:'100%',display:'flex',flexDirection:'column',justifyContent:'space-around',paddingLeft:'4%'}}  >
            
            
            
            
            <TextField
      
              className='formField'
              label="Amount Requested"
              name="amount_requested"
              type="number"
              value={amountRequested}
              onChange={(e)=>{setAmountRequested(e.target.value)}}
            />
       

            

            <TextField
             className='formField'
              label="Amount To Trade"
              name="amount_to_trade"
              type="number"
              value={amountToTrade}
              onChange={(e)=>{setAmountToTrade(e.target.value)}}
            />
        

            
            <Select className='formField' defaultValue="usd-to-lbp" sx={{width:'50%', borderRadius:'5%'}} id="transaction-type" onChange={(e)=>{if (e.target.value==="usd-to-lbp"){setTransactionType(true)}
              else{setTransactionType(false)}}} >
                <MenuItem value="usd-to-lbp" >USD to LBP</MenuItem>
                <MenuItem value="lbp-to-usd">LBP to USD</MenuItem>
               </Select>
       

            <Button onClick={addOffer} style={{width:'80px',padding:'8px',backgroundColor:'#0093d5'}} variant="contained" color="primary" >
              Add
            </Button>

      </form>
</Dialog>
    </div>
  );
}

export default CreateOffer;



