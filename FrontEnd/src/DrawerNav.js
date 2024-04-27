import React from "react";
import { Box, Drawer, IconButton, Stack, Typography,Menu,MenuItem } from "@mui/material";
import { useState } from "react";
import MenuIcon from '@mui/icons-material/Menu';
import './Nav.css'

const DrawerNav= ()=>
{

    const[isOpen, setIsOpen]= useState(false);
    const handleClick=()=> {setIsOpen(true)}
    const [anchorEl, setAnchorEl] = useState(null);
const handleMenu = (event) => {
  setAnchorEl(event.currentTarget);
};

const handleClose = () => {
  setAnchorEl(null);
};


  return (
    <Box sx={{display:{xs:'flex', md:'none', height:'100%', width:'20%',justifyContent:'center'}}}>

    <IconButton color="white" size="large" variant="contained" onClick={handleClick} >
        <MenuIcon color="white"/>
    </IconButton>

    <Drawer sx={{color:'white'}} aria-label="muiDrawer" anchor="left" open={isOpen} onClose= {() => setIsOpen(false)}>

        <Stack spacing={2} sx={{width:'70vw', height:'100%' ,display:'flex',alignItems:'center', bgcolor:'#d3d3d3'}}>
              <Box sx={{display:'flex',flexDirection:'row',width:'100%',height:'30%',justifyContent:'center',alignItems:'center', backgroundColor:'#0093d5'}}>
                  <Typography  color={"white"}>E.R</Typography>    
              </Box>

              <a class="navlinkdrawer" href="/transactions"><p>Transactions</p></a>
              <a class="navlinkdrawer" onClick={handleMenu}><p>Offers</p></a>
            <Menu sx={{
    "& .MuiList-root.MuiMenu-list": {
      padding: 0,
    },
  }}
              anchorEl={anchorEl}
              open={Boolean(anchorEl)}
              onClose={handleClose}
            >
              <MenuItem class='menuitem' onClick={handleClose}><a class='menulink' href='availible_offers'>Availible Offers </a></MenuItem>
              <MenuItem class='menuitem' onClick={handleClose}><a class='menulink' href='my_offers'>My Offers </a></MenuItem>
            </Menu>

            <a class="navlinkdrawer" href="/graph"><p>Graph</p></a>
            <a class="navlinkdrawer" href="/predictor"><p>Predictor</p></a>
            <a class="navlinkdrawer" href="/statistics"><p>Statistics</p></a>
              

        </Stack>


    </Drawer>






    </Box>
  )
}


export default DrawerNav;