import React, { Component } from 'react';
import Modal from "react-modal";
import { Button } from 'reactstrap';

class LoginBonusPopup extends Component {

	constructor(props) {
    super(props);
    this.state = {
      isOpen: this.props.isOpen,
      userName: this.props.userName
    };
  }

	handleToClose(e) {
		this.setState({isOpen: false});
	}

	render() {
    return (
      <div>
        <Modal
          isOpen={this.state.isOpen}
        >
         {this.state.userName}さん本日初ログインです。100円贈呈します。
        <Button id="closeBunnton" onClick={this.handleToClose.bind(this)}>閉じる</Button>
      </Modal>

	    </div>
    );
  }

}

export default LoginBonusPopup;
