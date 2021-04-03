import './App.css';
import React from "react";
import CreateGame from "./components/CreateGame";
import CreateKey from "./components/CreateKey";

function App() {
    return (
        <div className="App">
            <CreateGame/>
            <CreateKey/>
        </div>
    );
}

export default App;
