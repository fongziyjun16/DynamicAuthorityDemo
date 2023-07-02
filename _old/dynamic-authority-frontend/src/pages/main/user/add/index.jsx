import MainLayout from "@/pages/main/layout";
import {useState} from "react";
import {Button, Card, Col, Form, Input, notification, Row} from "antd";
import axios from "axios";

function AddUser() {

    const [loading, setLoading] = useState(false)
    const [notificationApi, contextHolder] = notification.useNotification()

    const signUpSubmit = (values) => {
        const {username, password, confirmPassword} = values
        if (password !== confirmPassword) {
            notificationApi.warning({
                message: "Notification",
                description: "Confirm Password is not same as Password"
            })
        } else {
            setLoading(true)
            axios.post(
                "/api/user/sign-up",
                {
                    username: username,
                    password: password
                },
                {
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded"
                    }
                }
            ).then(resp => {
                if (resp.data !== null) {
                    if (resp.data.code === 200) {
                        notificationApi.success({
                            message: "Notification",
                            description: "Sign Up Success"
                        })
                    } else {
                        notificationApi.warning({
                            message: "Notification",
                            description: resp.data.message
                        })
                    }
                }
                setLoading(false)
            }).catch(err => {
                console.log(err)
            })
        }
    }

    return (
        <>
            {contextHolder}
            <Card title="Sign Up">
                <Row>
                    <Col span={12}>
                        <Form
                            name="userSignUpForm"
                            labelCol={{span: 8}}
                            wrapperCol={{span: 16}}
                            onFinish={signUpSubmit}
                            autoComplete="off"
                        >
                            <Form.Item
                                label="Username"
                                name="username"
                                rules={[{required: true, message: 'Please input your username!'}]}
                            >
                                <Input/>
                            </Form.Item>

                            <Form.Item
                                label="Password"
                                name="password"
                                rules={[{required: true, message: 'Please input your password!'}]}
                            >
                                <Input.Password/>
                            </Form.Item>

                            <Form.Item
                                label="Confirm Password"
                                name="confirmPassword"
                                rules={[{required: true, message: 'Please input your password again!'}]}
                            >
                                <Input.Password/>
                            </Form.Item>

                            <Form.Item wrapperCol={{offset: 8, span: 16}}>
                                <Button type="primary" htmlType="submit" disabled={loading}>
                                    Submit
                                </Button>
                            </Form.Item>
                        </Form>
                    </Col>
                    <Col span={12}></Col>
                </Row>
            </Card>
        </>
    );
}

AddUser.pageLayout = MainLayout

export default AddUser