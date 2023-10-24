provider "aws" {
  region = "us-east-1"  # Change this to your desired AWS region
}

resource "aws_ecs_cluster" "cakemanagerservice_cluster" {
  name = "cakemanagerservice-cluster"
}

resource "aws_ecs_task_definition" "cakemanagerservice_task" {
  family                   = "cakemanagerservice-task"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  execution_role_arn       = aws_iam_role.ecs_execution_role.arn
  cpu                      = "256"  # Specify the desired CPU units
  memory                   = "512"  # Specify the desired memory in MiB

  container_definitions = jsonencode([
    {
      name  = "cakemanagerservice-container"
      image = "your-docker-image:latest"  # Replace with your Docker image URL
      portMappings = [
        {
          containerPort = 8088
          hostPort      = 8088
        }
      ]
    }
  ])
}

resource "aws_iam_role" "ecs_execution_role" {
  name = "ecs_execution_role"
  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Action = "sts:AssumeRole",
        Effect = "Allow",
        Principal = {
          Service = "ecs-tasks.amazonaws.com"
        }
      }
    ]
  })
}

resource "aws_ecs_service" "cakemanagerservice_service" {
  name            = "cakemanagerservice-service"
  cluster         = aws_ecs_cluster.cakemanagerservice_cluster.id
  task_definition = aws_ecs_task_definition.cakemanagerservice_task.arn
  launch_type     = "FARGATE"

  network_configuration {
    subnets = ["subnet-xxxxxxxxxxx", "subnet-yyyyyyyyyyy"]  # TODO Need to create subnets and add subnet IDs
    security_groups = [aws_security_group.ecs_security_group.id]
  }

  depends_on = [aws_iam_role_policy_attachment.ecs_execution_role_policy]
}

resource "aws_iam_role_policy_attachment" "ecs_execution_role_policy" {
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
  role       = aws_iam_role.ecs_execution_role.name
}

resource "aws_security_group" "ecs_security_group" {
  name_prefix = "ecs-"
  vpc_id      = "vpc-xxxxxxxxxxxxx"  # TODO Need to create VPC ID

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

output "service_url" {
  value = "http://${aws_ecs_service.cakemanagerservice_service.name}/"
}
