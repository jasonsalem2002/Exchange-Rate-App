import logo from './logo.svg';
import './App.css';
import React, { useCallback, useEffect } from 'react'
import { useState } from 'react';
import { Alert, AppBar, Box, Button, Link, MenuItem, Select, Snackbar, TextField, Toolbar, Typography } from '@mui/material';
import UserCredentialsDialog from './UserCredentialsDialog/UserCredentialsDialog';
import { getUserToken,saveUserToken, clearUserToken } from "./localstorage";
import { DataGrid } from '@mui/x-data-grid';
import Home from './Home';
import Offers from './Offers';
import { BrowserRouter as Router,Routes, Route, Switch,  NavLink, Redirect } from 'react-router-dom';
import { UserProvider } from './UserContext';


function App() {

  return (
    <div style={{}}>
      <UserProvider>
    <Router>
      <Routes>
        <Route path='/' element={<Home/>}></Route>
        <Route path='/rates' element={<Home/>}></Route>
        <Route path='/transactions' element={<Offers/>}></Route>
        <Route path='/offers' element={<Offers/>}></Route>
        <Route path='/offers' element={<Offers/>}></Route>
      </Routes>
    </Router>
    </UserProvider>
    </div>
  );
}

export default App;
