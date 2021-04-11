import React, {Component} from "react";
import axios from "axios";
import {Link, withRouter} from "react-router-dom";
import Button from "@material-ui/core/Button";
import {Avatar, TextField} from "@material-ui/core";
const axiosPOSTconfig = {headers: {'Content-Type': 'application/json'}};


class UpdateGame extends Component{

    constructor(props) {
        super(props);
        this.state = {
            rows: [],
            id: '',
            name: "",
            price: "",
            description: "",
            date: ""
        };
    }

    onChange = (event) => {
        this.setState({[event.target.id]: event.target.value});
    }

    onSubmit = (event) => {
        let {name, price, description, date} = this.state;
        event.preventDefault();
        axios.post(`http://localhost:8082/games/update/` + this.props.match.params.id, JSON.stringify({
            'name': name,
            'price': price,
            'description': description,
            'date': date,
            'id': this.props.match.params.id
        }), axiosPOSTconfig)
            .then((response) => {
                this.setState({status: response.data.status});
            })
            .catch((error) => {console.log(error)});
    }

    componentDidMount() {
        console.log(this.props);
        axios.get(`http://localhost:8082/games/update/`+this.props.match.params.id)
            .then((response) => {this.setState({name: response.data.game.rows[0].name, price: response.data.game.rows[0].price, description: response.data.game.rows[0].description, date: response.data.game.rows[0].date});})
            .catch((error) => {console.log(error); this.setState({ message: error.message })});
    }

    render() {
        let {name, price, description, date} = this.state;
        return(
            <main role="main" className="container">
                <div>
                        <form onSubmit={this.onSubmit}>
                            <TextField id="game" type="text" value={name} placeholder={"Enter new Name"} onChange={this.onChange}/><br/>
                            <TextField id="price" type="text" value={price} placeholder={"Enter new Price"} onChange={this.onChange}/><br/>
                            <TextField id="description" type="text" value={description} placeholder={"Enter new Description"} onChange={this.onChange}/><br/>
                            <TextField id="date" type="text" value={date} placeholder={"Enter new Date"} onChange={this.onChange}/><br/>

                            <br/><Button onClick={this.onSubmit} variant="contained" color="primary">Update Game</Button><br/>
                            <br/><Button component={Link} to="/Image" variant="contained" color="primary" >Add Image</Button>
                            <Button component={Link} to="/Games" variant="contained" color="primary" >Game's Table</Button><br/>
                        </form>
                </div>
            </main>
        );
    }
}

export default withRouter(UpdateGame);
