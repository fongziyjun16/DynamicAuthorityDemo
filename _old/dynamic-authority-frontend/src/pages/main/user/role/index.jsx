import MainLayout from "@/pages/main/layout";
import {Button, Card, Col, Form, Input, notification, Row} from "antd";
import {useState} from "react";
import axios from "axios";

function AssignRole2User() {
    const [notificationApi, contextHolder] = notification.useNotification();
    const [isAssignLoading, setIsAssignLoading] = useState(false)
    const [isRemoveLoading, setIsRemoveLoading] = useState(false)

    const notificationSuccess = (content) => {
        notificationApi.success({
            message: "Notification",
            description: content
        })
    }

    const notificationWarning = (content) => {
        notificationApi.warning({
            message: "Notification",
            description: content
        })
    }

    const assignSubmit = (values) => {
        setIsAssignLoading(true)
        axios.post(
            "/api/user/role",
            {
                username: values.username,
                roleName: values.roleName
            },
            {
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                }
            }
        ).then(resp => {
            if (resp.data != null) {
                const {code, message} = resp.data
                if (code === 200) {
                    notificationSuccess("Assign Successfully")
                } else {
                    notificationWarning(message)
                }
            }
            setIsAssignLoading(false);
        }).catch(err => {
            console.log(err)
        })
    }

    const removeSubmit = (values) => {
        setIsRemoveLoading(true)
        axios.delete(
            "/api/user/role?username=" + values.username + "&roleName=" + values.roleName
        ).then(resp => {
            if (resp.data != null) {
                const {code, message} = resp.data
                if (code === 200) {
                    notificationSuccess("Remove Successfully")
                } else {
                    notificationWarning(message)
                }
            }
            setIsRemoveLoading(false);
        }).catch(err => {
            console.log(err)
        })
    }

    return (
        <>
            {contextHolder}
            <Row>
                <Col span={24}>
                    <Card title="Assign Authority">
                        <Form
                            name="assignAuthorityForm"
                            layout="inline"
                            onFinish={assignSubmit}
                            autoComplete="off"
                        >
                            <Form.Item
                                label="Assign Role"
                                name="roleName"
                                rules={[{ required: true, message: 'Please input role name!' }]}
                            >
                                <Input/>
                            </Form.Item>
                            <Form.Item
                                label="to User"
                                name="username"
                                rules={[{ required: true, message: 'Please input username!' }]}
                            >
                                <Input/>
                            </Form.Item>
                            <Form.Item>
                                <Button type="primary" htmlType="submit" disabled={isAssignLoading}>
                                    Submit
                                </Button>
                            </Form.Item>
                        </Form>
                    </Card>
                </Col>
            </Row>
            <Row>
                <Col span={24}>
                    <Card title="Remove Authority">
                        <Form
                            name="removeAuthorityForm"
                            layout="inline"
                            onFinish={removeSubmit}
                            autoComplete="off"
                        >
                            <Form.Item
                                label="Remove Role"
                                name="roleName"
                                rules={[{ required: true, message: 'Please input role name!' }]}
                            >
                                <Input/>
                            </Form.Item>
                            <Form.Item
                                label="from User"
                                name="username"
                                rules={[{ required: true, message: 'Please input username!' }]}
                            >
                                <Input/>
                            </Form.Item>
                            <Form.Item>
                                <Button type="primary" htmlType="submit" disabled={isRemoveLoading}>
                                    Submit
                                </Button>
                            </Form.Item>
                        </Form>
                    </Card>
                </Col>
            </Row>
        </>
    )
}

AssignRole2User.pageLayout = MainLayout

export default AssignRole2User