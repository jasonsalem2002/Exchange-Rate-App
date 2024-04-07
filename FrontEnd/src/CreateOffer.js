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


function CreateOffer() {

   const [dialogOpen, setDialogOpen] = useState(false);
   const [formData, setFormData] = useState({
    amountOffered:'',
    amountRequested: '',
    exchangeRate: '',
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevFormData) => ({
      ...prevFormData,
      [name]: value,
    }));
  };

   return(
   <div>
    <Button sx={{backgroundColor:'#0093d5',color:'white'}} onClick={()=>setDialogOpen(true)}>Create Offer</Button>
    <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)}>
    <Box sx={{backgroundColor:'#0093d5',height:'86px',display:'flex',flexDirection:'column',justifyContent:'center',paddingLeft:'4%'}}><Typography fontWeight={'bold'} color={'white'} variant='h6'>Create Offer</Typography></Box>
    <form style={{height:'500px',width:'600px',display:'flex',flexDirection:'column',justifyContent:'space-around',paddingLeft:'4%'}}  >
            
            
            <Box sx={{height:'100px',display:'flex',flexDirection:'column',justifyContent:'space-around'}}>
              
            <TextField
              sx={{width:'50%', borderRadius:'5%'}}
              label="Amount Requested"
              name="amountRequested"
              value={formData.amountRequested}
              onChange={handleChange}
            />
            </Box>

            <Box sx={{height:'100px',display:'flex',flexDirection:'column',justifyContent:'space-around'}}>

            <TextField
             sx={{width:'50%', borderRadius:'5%'}}
              label="Amount Offered"
              name="amountOffered"
              value={formData.amountOffered}
              onChange={handleChange}
            />
            </Box>
        
            <Box sx={{height:'100px',display:'flex',flexDirection:'column',justifyContent:'space-around'}}>
            <TextField
             sx={{width:'50%', borderRadius:'5%'}}
              label="Exchange Rate"
              name="exchangeRate"
              value={formData.exchangeRate}
              onChange={handleChange}
            />
            </Box>

         
            <Button style={{width:'80px',padding:'8px',backgroundColor:'#0093d5'}} variant="contained" color="primary" type="submit">
              Add
            </Button>

      </form>
</Dialog>
    </div>
  );
}

export default CreateOffer;
