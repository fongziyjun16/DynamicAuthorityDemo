import MainLayout from "@/pages/main/layout";
import {useState} from "react";
import {Button, Card, Col, Form, Input, notification, Row, Select} from "antd";
import {selectMethod} from "@/pages/main/components/common";
import axios from "axios";

function ResourceUpdate() {

    const [notificationApi, contextHolder] = notification.useNotification();
    const [loading, setLoading] = useState(false)

    const updateResource = (values) => {
        setLoading(true)
      axios.put(
          "/api/resource/authorization-type?method=" + values.method + "&path=" + values.path + "&authorizationType=" + values.authorizationType
      ).then(resp => {
          if (resp.data != null) {
              const {code, message} = resp.data
              if (code === 200) {
                  notificationApi.success({
                      message: "Notification",
                      description: "Update Successfully"
                  })
              } else {
                  notificationApi.warning({
                      message: "Notification",
                      description: message
                  })
              }
          }
          setLoading(false)
      }).catch(err => {
          console.log(err)
      })
    }

    return (
        <>
            {contextHolder}
            <Card title="Update Resource">
                <Row>
                    <Col span={12}>
                        <Form
                            name="basic"
                            labelCol={{span: 8}}
                            wrapperCol={{span: 16}}
                            onFinish={updateResource}
                            autoComplete="off"
                        >
                            <Form.Item
                                label="Method"
                                name="method"
                                rules={[{required: true, message: 'Please input method!'}]}
                            >
                                {selectMethod}
                            </Form.Item>

                            <Form.Item
                                label="Path"
                                name="path"
                                rules={[{required: true, message: 'Please input path!'}]}
                            >
                                <Input/>
                            </Form.Item>

                            <Form.Item
                                label="Authorization Type"
                                name="authorizationType"
                                rules={[{required: true, message: 'Please select authorization type!'}]}
                            >
                                <Select
                                    options={[
                                        {
                                            value: "NO_AUTH",
                                            label: "NO_AUTH"
                                        },
                                        {
                                            value: "ANY",
                                            label: "ANY"
                                        },
                                        {
                                            value: "ALL",
                                            label: "ALL"
                                        },
                                    ]}
                                />
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
    )
}

ResourceUpdate.pageLayout = MainLayout

export default ResourceUpdate