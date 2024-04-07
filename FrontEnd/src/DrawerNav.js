import React from "react";
import { Box, Drawer, IconButton, Stack, Typography } from "@mui/material";
import { useState } from "react";
import MenuIcon from '@mui/icons-material/Menu';

const DrawerNav= ()=>
{

    const[isOpen, setIsOpen]= useState(false);
    const handleClick=()=> {setIsOpen(true)}



  return (
    <Box sx={{display:{xs:'flex', md:'none', height:'100%', width:'20%',justifyContent:'center'}}}>

    <IconButton color="white" size="large" variant="contained" onClick={handleClick} >
        <MenuIcon color="white"/>
    </IconButton>

    <Drawer sx={{color:'white'}} aria-label="muiDrawer" anchor="left" open={isOpen} onClose= {() => setIsOpen(false)}>

        <Stack spacing={2} sx={{width:'70vw', height:'100%' ,display:'flex',alignItems:'center', bgcolor:'#d3d3d3'}}>
              <Box sx={{display:'flex',flexDirection:'row',width:'100%',height:'30%',justifyContent:'center',alignItems:'center', backgroundColor:'#0093d5'}}>
                  <Typography variant="h5" color={"white"}>EXCHANGE RATE</Typography>    
              </Box>

              <a class="navlinkdrawer" href="/home"><p>Rates</p></a>
              <a class="navlinkdrawer" href="/apparel"><p>Transactions</p></a>
              <a class="navlinkdrawer" href="/home"><p>Offers</p></a>
              <a class="navlinkdrawer" href="/apparel"><p>Calculator</p></a>

        </Stack>


    </Drawer>






    </Box>
  )
}


export default DrawerNav;