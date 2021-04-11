import React, {Component} from "react";
import axios from 'axios';
import {withRouter, Redirect, Link} from 'react-router-dom';
import * as PropTypes from "prop-types";
import {func} from "prop-types";


Redirect.propTypes = {to: PropTypes.string};

class DeleteGame extends Component {

    constructor(props) {
        super(props);
        this.state = {
            status: ''
        };
    }

    componentDidMount() {
        console.log(this.props);
        axios.delete('http://localhost:8082/games/delete/' + this.props.match.params.id)
            .then((response) => {
                this.setState({status: response.data.status});
            })
            .catch((error) => {
                console.log(error);
                this.setState({message: error.message})
            });
    }

    render() {
        return (
                <Redirect to={"/Games"}/>
        );
        // if (this.state.status === 1) {
        //     return (
        //         <Redirect to={'/subdivisions/' + this.props.match.params.id + '/employees'}/>
        //     );
        // }
        // return (
        //     <div>Error: {this.state.message}</div>
        // );
    }
}

export default withRouter(DeleteGame);
