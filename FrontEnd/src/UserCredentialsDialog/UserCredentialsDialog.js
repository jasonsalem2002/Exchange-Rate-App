import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import TextField from '@mui/material/TextField';
import React, { useState } from "react";
import "./UserCredentialsDialog.css";
import { User } from '../UserContext';

export default function UserCredentialsDialog({open,onSubmit,onClose,title,submitText}) {
const {userName}=User();
const {setUserName}= User()
 let [password, setPassword] = useState("");
 
 return (
 <Dialog open={open} onClose={onClose} maxWidth="xs" fullWidth>
 <div className="dialog-container">
 <DialogTitle>{title}</DialogTitle>
 <div className="form-item">
 <TextField
 fullWidth
 label="Username"
 type="text"
 value={userName}
 onChange={({ target: { value } }) => setUserName(value)}
 />
 </div>
 <div className="form-item">
 <TextField
 fullWidth
 label="Password"
 type="password"
 value={password}
 onChange={({ target: { value } }) => setPassword(value)}
 onKeyPress={(event)=>{if (event.key === 'Enter') {
    onSubmit(userName, password);
  }}}
 />
 </div>
 <Button
 color="primary"
 variant="contained"
 onClick={() => onSubmit(userName, password)}>{submitText} </Button>
 </div>
 </Dialog>
 );
}


