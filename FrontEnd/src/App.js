import './App.css';
import React, { useCallback, useEffect } from 'react'
import { useState } from 'react';
import { Alert, AppBar, Box, Button, Link, MenuItem, Select, Snackbar, TextField, Toolbar, Typography } from '@mui/material';
import UserCredentialsDialog from './UserCredentialsDialog/UserCredentialsDialog';
import { getUserToken,saveUserToken, clearUserToken } from "./localstorage";
import { DataGrid } from '@mui/x-data-grid';
import Home from './Home';
import AvailibleOffers from './AvailibleOffers';
import { BrowserRouter as Router,Routes, Route, Switch,  NavLink, Redirect } from 'react-router-dom';
import { UserProvider } from './UserContext';
import Graph from './Graph';
import MyOffers from './MyOffers';
import AvailableOffers from './AvailibleOffers';
import Predictor from './Predictor';
import Transactions from './Transactions/Transactions';


function App() {
  //put offers
  return (
    <div style={{}}>
    <UserProvider>
    <Router>
      <Routes>
        <Route path='/' element={<Home/>}></Route>
        <Route path='/rates' element={<Home/>}></Route>
        <Route path='/transactions' element={<Transactions/>}></Route>
        <Route path='/availible_offers' element={<AvailableOffers/>}></Route>
        <Route path='/my_offers' element={<MyOffers/>}></Route>
        <Route path='/graph' element={<Graph/>}></Route>
        <Route path='/predictor' element={<Predictor/>}></Route>
      </Routes>
    </Router>
    </UserProvider>
    </div>
  );
}

export default App;
